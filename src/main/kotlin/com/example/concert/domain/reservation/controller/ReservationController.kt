package com.example.concert.domain.reservation.controller

import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.dto.response.ReservationResponseDto
import com.example.concert.domain.reservation.service.ReservationService
import com.example.concert.util.response.Response
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/reservation")
class ReservationController(private val reservationService: ReservationService) {

    @PostMapping
    @RequestMapping("/register")
    fun reservationRegistration(@AuthenticationPrincipal memberDetails: UserDetails,
                                @Valid @RequestBody reservationRequestDto: ReservationRequestDto,
                                )
    : ResponseEntity<Response<ReservationResponseDto>>{
        val memberName = memberDetails.username
        val response =Response(
            status = HttpStatus.OK.value(),
            message = "Successfully completed reservation.",
            data = reservationService.makeReservation(reservationRequestDto, memberName)
        )

        return ResponseEntity(response, HttpStatus.OK)


    }

    @GetMapping
    @RequestMapping("/list")
    fun getReservationList(@AuthenticationPrincipal memberDetails: UserDetails)
    : ResponseEntity<Response<MutableList<ReservationResponseDto>>>{
        val memberName = memberDetails.username
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully got the reservation list.",
            data = reservationService.getReservationList(memberName)
        )

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping
    @RequestMapping("/cancel/{reservationId}")
    fun cancelReservation(@AuthenticationPrincipal memberDetails: UserDetails, @PathVariable reservationId : UUID)
    :ResponseEntity<Response<String>>{
        val memberName = memberDetails.username
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully canceled reservation.",
            data = reservationService.cancelReservation(memberName, reservationId                  )
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

}