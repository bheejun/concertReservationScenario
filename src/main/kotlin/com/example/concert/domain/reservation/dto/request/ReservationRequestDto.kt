package com.example.concert.domain.reservation.dto.request

import com.example.concert.util.StringListToStringConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import java.util.*

data class ReservationRequestDto(
    val scheduleId : UUID,

    val seatNum : MutableList<Int>

)
