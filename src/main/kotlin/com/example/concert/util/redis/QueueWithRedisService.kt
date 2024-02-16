package com.example.concert.util.redis

import com.example.concert.exception.AuthenticationFailureException
import com.example.concert.exception.NotFoundException
import kotlinx.coroutines.*
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

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
            val available = lock.tryLock(10, 1, TimeUnit.SECONDS)

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
        val bucket = redissonClient.getBucket<Any>(memberId)
        bucket.set(prefixWorkingQueue + scheduleId, Duration.ofSeconds(10L))
    }

    fun getWaitingQueueNum(scheduleId: String, memberId: String): Long {
        val waitingQueue = redissonClient.getScoredSortedSet<String>(prefixWaitingQueue + scheduleId)
        val num : Long = try {
            waitingQueue.rank(memberId).toLong()
        }catch (e: Exception){
            -1L
        }
        return num
    }

    fun leaveWaitingQueue(scheduleId: String, memberId: String) {
        val waitingQueue = redissonClient.getScoredSortedSet<String>(prefixWaitingQueue + scheduleId)
        waitingQueue.remove(memberId)
    }

    fun leaveWorkingQueue(scheduleId: String, memberId: String) {
        val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
        workingQueue.remove(memberId)

        val bucket = redissonClient.getBucket<Any>(memberId)
        bucket.delete()
    }

    fun processQueue(scheduleId: String) {
        val lock = redissonClient.getLock(scheduleId)
        try {
            val available = lock.tryLock(10, 1, TimeUnit.SECONDS)

            if (!available) {
                throw NotFoundException("Cannot found Lock")
            }

            val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
            val currentWorkingQueueSize = workingQueue.size.toLong()

            if (currentWorkingQueueSize <= 10) {
                val availableSlots = 10 - currentWorkingQueueSize
                if(availableSlots>0){
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
        val membersInWaitingQueue = waitingQueue.pollFirst(availableSlots.toInt())

        membersInWaitingQueue.forEach { memberId ->
            addWorkingQueue(scheduleId, memberId)
            leaveWaitingQueue(scheduleId, memberId)
        }
    }

    fun isAlreadyInWorkingQueue(scheduleId: String, memberId: String): Boolean {
        val workingQueue = redissonClient.getSet<String>(prefixWorkingQueue + scheduleId)
        return workingQueue.contains(memberId)
    }

    fun startCheck(scheduleId: String, memberId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                try {
                    if (!expireStatus(scheduleId, memberId)) {
                        delay(1000)
                    } else {
                        break
                    }
                } catch (e: AuthenticationFailureException) {
                    throw e
                }
            }
        }
    }

    fun expireStatus(scheduleId: String, memberId: String): Boolean {
        val bucket = redissonClient.getBucket<Any>(memberId)
        val value = bucket.get()

        if (value == null) {
            if (isAlreadyInWorkingQueue(scheduleId, memberId)) {
                logger.info("in workingQueue and expired")
                leaveWorkingQueue(scheduleId, memberId)
                processQueue(scheduleId)
                return true
            } else {
                logger.info("not in workingQueue and expired")
                return true
            }
        }else{
            if (isAlreadyInWorkingQueue(scheduleId, memberId)) {
                logger.info("in workingQueue and not expired")
                return false
            } else {
                logger.info("not in workingQueue and not expired")
                return false
            }
        }
    }

}