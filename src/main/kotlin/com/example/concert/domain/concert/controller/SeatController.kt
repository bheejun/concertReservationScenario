package com.example.concert.domain.concert.controller

import com.example.concert.domain.concert.dto.response.SeatStatusResponseDto
import com.example.concert.domain.concert.service.SeatService
import com.example.concert.util.redis.QueueWithRedisService
import com.example.concert.util.response.Response
import com.example.concert.util.security.SecurityCoroutineContext
import com.example.concert.util.security.userdetails.UserDetailsImpl
import kotlinx.coroutines.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/concert/seat")
class SeatController(
    private val seatService: SeatService,
    private val queueWithRedisService: QueueWithRedisService
) {

    @GetMapping
    @RequestMapping("/{scheduleId}")
    fun getSeatStatus(
        @AuthenticationPrincipal member: UserDetailsImpl,
        @PathVariable scheduleId: UUID
    ): ResponseEntity<Response<MutableList<SeatStatusResponseDto>>> {

        if (queueWithRedisService.isAlreadyInWorkingQueue(scheduleId.toString(), member.getMemberId().toString())
        ) {
            val response = Response(
                status = HttpStatus.OK.value(),
                message = "Successfully got the seat information",
                data = seatService.getSeatsStatus(scheduleId)
            )
            return ResponseEntity(response, HttpStatus.OK)
        } else {
            runBlocking {
                checkNumUntilInWorkingQueue(scheduleId, member.getMemberId())
            }
            val response = Response(
                status = HttpStatus.OK.value(),
                message = "Successfully got the seat information",
                data = seatService.getSeatsStatus(scheduleId)
            )
            return ResponseEntity(response, HttpStatus.OK)
        }
    }

    private suspend fun checkNumUntilInWorkingQueue(
        scheduleId: UUID, memberId: UUID
    ) {
        withContext(Dispatchers.IO + SecurityCoroutineContext()) {
            while (!queueWithRedisService.isAlreadyInWorkingQueue(scheduleId.toString(), memberId.toString())) {
                delay(1000L)
//                val seatCount = scheduleRepository.findById(scheduleId)
//                    .orElseThrow { NotFoundException("The Schedule not found for provided id") }.extraSeatCount
//                if (seatCount == 0) {
//
//                }
                if (queueWithRedisService.getWaitingQueueNum(scheduleId.toString(), memberId.toString()) == -1L) {
                    break

                }
            }
            delay(1000L)
        }
    }
}