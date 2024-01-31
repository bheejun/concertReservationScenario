package com.example.concert.domain.reservation.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class ReservationResponseDto(
    val reservationId : UUID,
    val memberName : String,
    val concertName : String,
    val concertDate : LocalDateTime,
    val ticketNum : Int,
    val seatNum : MutableList<Int>,
    val totalPrice : Double,
    val reservationDate : LocalDateTime
)
