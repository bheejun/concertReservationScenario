package com.example.concert.domain.concert.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class ConcertResponseDto(
    val concertId : UUID,
    val concertName : String,
    val artist : String,
    val concertDate : MutableList<LocalDateTime>,
    val ticketPrice : Double,
    val scheduleId : MutableList<UUID>

)
