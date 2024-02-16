package com.example.concert.domain.reservation.service

import com.example.concert.domain.member.model.Member
import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.dto.response.ReservationResponseDto
import java.util.UUID

interface ReservationService {
    fun makeReservation(reservationRequestDto: ReservationRequestDto, memberId : UUID): ReservationResponseDto

    fun getReservationList(member : Member) : MutableList<ReservationResponseDto>

    fun getReservation(reservationId : UUID, memberId : UUID) : ReservationResponseDto

    fun cancelReservation(memberId : UUID, reservationId: UUID) : String

}