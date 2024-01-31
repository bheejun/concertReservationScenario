package com.example.concert.domain.reservation.service

import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.dto.response.ReservationResponseDto
import java.util.UUID

interface ReservationService {
    fun makeReservation(reservationRequestDto: ReservationRequestDto, memberName:String): ReservationResponseDto

    fun getReservationList(memberName: String) : MutableList<ReservationResponseDto>

    fun getReservation(reservationId : UUID, memberName: String) : ReservationResponseDto
}