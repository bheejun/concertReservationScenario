package com.example.concert.domain.payment

import java.util.UUID

data class PaymentRequestDto(
    val reservationId : UUID,
    val paymentType : String
)
