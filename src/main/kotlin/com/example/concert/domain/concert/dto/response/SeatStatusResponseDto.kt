package com.example.concert.domain.concert.dto.response

import jakarta.transaction.Status
import java.util.UUID

data class SeatStatusResponseDto(
    val seatId :UUID,
    val seatNum : Int,
    val isBooked : Boolean
)
