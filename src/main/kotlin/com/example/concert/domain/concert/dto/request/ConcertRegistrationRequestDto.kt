package com.example.concert.domain.concert.dto.request

data class ConcertRegistrationRequestDto(
    val artist :String,
    val concertName: String,
    val concertSchedule : List<String>,
    val ticketPrice : Double
)