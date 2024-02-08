package com.example.concert.util.event

import com.example.concert.util.redis.RedisService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ReservationEventListener(private val redisService: RedisService) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun detectReservationCompleteEvent(event: ReservationCompleteEvent){
        redisService.leaveWorkingQueue(event.scheduleId, event.memberId)
        redisService.processQueue(event.scheduleId)
    }
}