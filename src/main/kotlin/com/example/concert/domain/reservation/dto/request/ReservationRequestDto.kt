package com.example.concert.domain.reservation.dto.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.*

data class ReservationRequestDto(
    @field: NotNull(message = "This field cannot be null")
    val seatIdList : List<UUID>
)

