package com.example.concert.domain.concert.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class ConcertOnSpecificDateResponseDto(
    val concertId : UUID,
    val scheduleId : UUID,
    val concertName : String,
    val artist : String,
    val concertDate : LocalDateTime,
    val ticketPrice : Double,
    val extraSeatNum : Int,
    val availableSeatNum : MutableList<Int>

)
