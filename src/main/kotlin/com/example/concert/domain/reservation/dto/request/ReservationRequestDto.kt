package com.example.concert.domain.reservation.dto.request

import com.example.concert.util.StringListToStringConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import java.util.*

data class ReservationRequestDto(
    val concertId : UUID,

    val seat : List<String>



)
