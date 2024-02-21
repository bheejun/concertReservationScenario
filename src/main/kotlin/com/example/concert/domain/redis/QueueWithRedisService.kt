package com.example.concert.domain.redis

import com.example.concert.exception.AuthenticationFailureException
import com.example.concert.exception.NotFoundException
import com.example.concert.util.security.SecurityCoroutineContext
import kotlinx.coroutines.*
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Service
class QueueWithRedisService(
    private val redissonClient: RedissonClient
) {
    private val logger = LoggerFactory.getLogger(QueueWithRedisService::class.java)
    private val prefixWorkingQueue = "Work"
    private val prefixWaitingQueue = "Wait"

    fun addQueueFilter(scheduleId: String, memberId: String) {
        val lock = redissonClient.getLock(scheduleId)
        try {
            val available = lock.tryLock(3, 2, TimeUnit.SECONDS)

            if (!available) {
                throw NotFoundException("Cannot found Lock")
            }
            val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
            val waitingQueue = redissonClient.getScoredSortedSet<String>(prefixWaitingQueue + scheduleId)
            val workingQueueSize = workingQueue.size.toLong()
            val waitingQueueSize = waitingQueue.size().toLong()

            if (workingQueueSize < 10 && waitingQueueSize == 0L) {
                addWorkingQueue(scheduleId, memberId)
            } else {
                addWaitingQueue(scheduleId, memberId)
            }
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }
    }

    private fun addWaitingQueue(scheduleId: String, memberId: String) {
        val waitingQueue = redissonClient.getScoredSortedSet<String>(prefixWaitingQueue + scheduleId)
        waitingQueue.add(System.currentTimeMillis().toDouble(), memberId)
    }

    private fun addWorkingQueue(scheduleId: String, memberId: String) {
        val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
        if (!workingQueue.contains(memberId)) {
            workingQueue.add(memberId)
            addSecondIndex(scheduleId, memberId)
        }
    }

    private fun addSecondIndex(scheduleId: String, memberId: String) {
        val key = "jun:$scheduleId:$memberId"
        redissonClient.getBucket<String>(key).set(System.currentTimeMillis().toString(), Duration.ofSeconds(20L))
    }

    fun getWaitingQueueNum(scheduleId: String, memberId: String): Long {
        val waitingQueue = redissonClient.getScoredSortedSet<String>(prefixWaitingQueue + scheduleId)
        val num: Long = try {
            waitingQueue.rank(memberId).toLong()
        } catch (e: Exception) {
            -1L
        }
        return num
    }

    fun leaveWorkingQueue(scheduleId: String, memberId: String) {
        val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
        logger.warn("WQ ${workingQueue}")
        workingQueue.remove(memberId)

        val bucket = redissonClient.getBucket<String>("jun:$scheduleId:$memberId")
        bucket.delete()
    }
    fun processQueue(scheduleId: String) {
        val lock = redissonClient.getLock(scheduleId)
        try {
            val available = lock.tryLock(3, 2, TimeUnit.SECONDS)

            if (!available) {
                throw NotFoundException("Cannot found Lock")
            }

            val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
            val currentWorkingQueueSize = workingQueue.size.toLong()

            if (currentWorkingQueueSize < 10) {
                val availableSlots = 10 - currentWorkingQueueSize
                logger.warn("available : ${availableSlots}")
                if (availableSlots > 0) {
                    moveFromWaitingToWorkingQueue(scheduleId, availableSlots)
                }
            }

        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }


    }
    private fun moveFromWaitingToWorkingQueue(scheduleId: String, availableSlots: Long) {
        val waitingQueue = redissonClient.getScoredSortedSet<String>(prefixWaitingQueue + scheduleId)

        val memberList = waitingQueue.pollFirst(availableSlots.toInt())
        memberList.forEach{ memberId ->
            addWorkingQueue(scheduleId, memberId)
        }


    }

    fun isAlreadyInWorkingQueue(scheduleId: String, memberId: String): Boolean {
        val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
        return workingQueue.contains(memberId)
    }

    fun startCheck(scheduleId: String, memberId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (expireStatus(scheduleId, memberId)) {
                    logger.warn("퇴장${memberId}")
                    break
                }
                delay(1000L)
            }
        }
    }

    fun expireStatus(scheduleId: String, memberId: String): Boolean {

        val secondIndex = redissonClient.getBucket<String>(memberId)
        val value = secondIndex.get()
        logger.warn(value)

        if (value == null) {
            if (isAlreadyInWorkingQueue(scheduleId, memberId)) {
                logger.warn("in workingQueue and expired")
                leaveWorkingQueue(scheduleId, memberId)
                processQueue(scheduleId)
                return true
            } else {
                logger.warn("not in workingQueue and expired. 예약완료 혹은 예외")
                leaveWorkingQueue(scheduleId, memberId)
                processQueue(scheduleId)
                return true
            }
        } else {
            if (isAlreadyInWorkingQueue(scheduleId, memberId)) {
                logger.info("in workingQueue and not expired")
                return false
            } else {
                logger.info("not in workingQueue and not expired")
                return false
            }
        }


    }

   suspend fun checkNumUntilInWorkingQueue(scheduleId: String, memberId: String) {
       withContext(Dispatchers.IO + SecurityCoroutineContext()) {
           while (!isAlreadyInWorkingQueue(scheduleId.toString(), memberId.toString())) {
               delay(1000L)
//                val seatCount = scheduleRepository.findById(scheduleId)
//                    .orElseThrow { NotFoundException("The Schedule not found for provided id") }.extraSeatCount
//                if (seatCount == 0) {
//
//                }
               if (getWaitingQueueNum(scheduleId.toString(), memberId.toString()) == -1L) {
                   break

               }
           }
           delay(1000L)
       }
    }


    fun createPaymentToken(reservationId: String) {
        val bucket = redissonClient.getBucket<Any>(reservationId)
        bucket.set(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString(), Duration.ofSeconds(300L))
    }

    fun checkPaymentTokenExist(reservationId: String): Boolean {
        val bucket = redissonClient.getBucket<Any>(reservationId)
        if (bucket.get() == null) {
            return false
        } else {
            return true
        }

    }


}