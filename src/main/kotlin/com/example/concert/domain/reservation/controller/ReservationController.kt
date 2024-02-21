package com.example.concert.domain.reservation.controller

import com.example.concert.domain.redis.QueueWithRedisService
import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.dto.response.ReservationResponseDto
import com.example.concert.domain.reservation.service.ReservationService
import com.example.concert.util.response.Response
import com.example.concert.util.security.userdetails.UserDetailsImpl
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/reservation")
class ReservationController(
    private val reservationService: ReservationService,
    private val queueWithRedisService: QueueWithRedisService
) {

    @PostMapping("/register/{scheduleId}")
    fun reservationRegistration(
        @AuthenticationPrincipal memberDetails: UserDetailsImpl,
        @Valid @RequestBody reservationRequestDto: ReservationRequestDto,
        @PathVariable scheduleId: UUID
    ): ResponseEntity<Response<ReservationResponseDto>> {
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully completed reservation.",
            data = reservationService.makeReservation(reservationRequestDto, memberDetails.getMemberId())
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/list")
    fun getReservationList(
        @AuthenticationPrincipal memberDetails: UserDetailsImpl
    ): ResponseEntity<Response<MutableList<ReservationResponseDto>>> {
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully got the reservation list.",
            data = reservationService.getReservationList(memberDetails.getMember())
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{reservationId}")
    fun getReservation(
        @AuthenticationPrincipal memberDetails: UserDetailsImpl,
        reservationId: UUID
    ): ResponseEntity<Response<ReservationResponseDto>> {
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully got the reservation list.",
            data = reservationService.getReservation(reservationId, memberDetails.getMember().id!!)
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/cancel/{reservationId}")
    fun cancelReservation(
        @AuthenticationPrincipal memberDetails: UserDetailsImpl,
        @PathVariable reservationId: UUID
    ): ResponseEntity<Response<String>> {
        val memberId = memberDetails.getMemberId()
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully canceled reservation.",
            data = reservationService.cancelReservation(memberId, reservationId)
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/check/{reservationId}")
    fun checkPaymentStatus(@PathVariable reservationId: String): ResponseEntity<Boolean> {
        return ResponseEntity(queueWithRedisService.checkPaymentTokenExist(reservationId), HttpStatus.OK)
    }

}