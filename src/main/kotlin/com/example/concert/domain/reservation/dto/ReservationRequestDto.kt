package com.example.concert.domain.reservation.dto

import com.example.concert.util.StringListToStringConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import java.util.*

data class ReservationRequestDto(

    val numberOfTicket : Int,

    val seat : List<String>,

    val totalPrice : Double



)
