package com.example.concert.domain.concert.dto.response

import java.time.LocalDateTime

data class GetAvailableDateResponseDto(
    val availableDate : MutableList<LocalDateTime> = mutableListOf(),
    val unavailableDates :MutableList<LocalDateTime> = mutableListOf(),
    val result : String
)
