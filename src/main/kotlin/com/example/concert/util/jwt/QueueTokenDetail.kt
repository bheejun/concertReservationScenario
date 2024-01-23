package com.example.concert.util.jwt

import java.util.UUID

data class QueueTokenDetail(
    val memberUUID : UUID,
    val queuePosition : Int
)
