package com.example.concert.domain.concert.dto.response

import java.util.UUID

data class SeatStatusResponseDto(
    val seatId :UUID,
    val seatNum : Int,
    val isBooked : Boolean
)
