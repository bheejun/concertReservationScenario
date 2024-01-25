package com.example.concert.domain.reservation.service

import com.example.concert.domain.reservation.dto.request.ReservationRequestDto

interface ReservationService {
    fun makeReservation(reservationRequestDto: ReservationRequestDto)
}