package com.example.concert.util.redis

import com.example.concert.domain.member.model.Member
import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.exception.NotFoundException
import com.example.concert.util.event.ReservationEventListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<Any, Any>,
    private val memberRepository: MemberRepository
) {

    fun addWaitingQueue(scheduleId: UUID, memberId: UUID) {
        /*add sorted set(key, member(value), score):
         schedule id is key, member id is member(in sorted set, value is called member)
         currentTime is score. Score must be Double. So this queue will be sorted by currentTime(score)*/
        redisTemplate.opsForZSet().add(scheduleId, memberId, System.currentTimeMillis().toDouble())
    }
    fun addWorkingQueue(scheduleId: UUID, memberId: UUID){
        redisTemplate.opsForSet().add(scheduleId, memberId,)
    }

    fun getQueueNum(scheduleId: UUID, memberId: UUID): Long {
        redisTemplate.opsForZSet().range(scheduleId, 0, -1)
            ?: throw NotFoundException("Current queue is empty now")

        return redisTemplate.opsForZSet().rank(scheduleId, memberId)
            ?: throw NotFoundException("The Member was not found in the current queue")
    }

    fun addQueueFilter(scheduleId: UUID, member: Member) {
        val memberId = member.id ?: throw NotFoundException("The member id was not found for provided member")
        val workingQueue = redisTemplate.opsForSet().size(scheduleId) ?: 0

        if (checkAlreadyInWorkingQueue(scheduleId, memberId)) {
            return
        }
        if (workingQueue <= 10) {
            addWorkingQueue(scheduleId, memberId)
            return

        }else{
            addWaitingQueue(scheduleId, memberId)
            return
        }


    }

    fun leaveWaitingQueue(scheduleId: UUID, memberId: UUID) {
        redisTemplate.opsForZSet().remove(scheduleId, memberId)
    }

    fun leaveWorkingQueue(scheduleId: UUID, memberId: UUID) {
        redisTemplate.opsForSet().remove(scheduleId, memberId)
    }

    @Scheduled(fixedRate = 1000)
    fun processQueue(scheduleId: UUID) {
        val currentProcessQueueSize = redisTemplate.opsForSet().size(scheduleId) ?: 0

        if (currentProcessQueueSize <= 10) {
            val availableSlots = 10 - currentProcessQueueSize
            moveFromWaitingToProcessQueue(scheduleId, availableSlots)
        }
    }

    fun moveFromWaitingToProcessQueue(scheduleId: UUID, availableSlots: Long) {
        val membersInWaitingQueue = redisTemplate.opsForZSet().range(scheduleId, 0, availableSlots - 1) ?: return
        if (membersInWaitingQueue.isNotEmpty()) {
            val membersIdList: Set<UUID> =
                membersInWaitingQueue.filterIsInstance<String>().map { UUID.fromString(it) }.toSet()

            membersIdList.forEach { memberId ->
                val member = memberRepository.findById(memberId).orElseThrow {
                    NotFoundException ("The Member was not found for provided id")}
                addQueueFilter(scheduleId, member)
                leaveWaitingQueue(scheduleId, memberId)

            }
        }

    }

    private fun checkAlreadyInWorkingQueue(scheduleId: UUID, memberId: UUID): Boolean {
        redisTemplate.opsForSet().isMember(scheduleId, memberId) ?: return false

        return true
    }


}