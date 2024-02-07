package com.example.concert.domain.concert.dto.response

import java.time.LocalDateTime

data class ConcertUpdateResponseDto(
    val updatedArtistName : String,
    val updatedConcertName :String,
    val updatedConcertDate : LocalDateTime,
    val updatedConcertPrice : Double
)
