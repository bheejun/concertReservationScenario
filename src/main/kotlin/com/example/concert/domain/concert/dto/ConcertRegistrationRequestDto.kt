package com.example.concert.domain.concert.dto

data class ConcertRegistrationRequestDto(
    val artist :String,
    val concertName: String,
    val concertDate : List<String>,
    val ticketPrice : Double

)