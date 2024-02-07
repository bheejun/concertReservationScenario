package com.example.concert.domain.concert.dto.request

data class ConcertUpdateRequestDto(
    val artist :String?,
    val concertName: String?,
    val concertDate : String?,
    val ticketPrice : Double?
)
