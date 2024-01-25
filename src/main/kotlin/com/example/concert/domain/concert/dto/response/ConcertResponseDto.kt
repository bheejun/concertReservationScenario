package com.example.concert.domain.concert.dto.response

import java.time.LocalDateTime

data class ConcertResponseDto(
    val concertName : String,
    val artist : String,
    val concertDate : LocalDateTime,
    val ticketPrice : Double
)
