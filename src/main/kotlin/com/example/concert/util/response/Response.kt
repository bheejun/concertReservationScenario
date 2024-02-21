package com.example.concert.util.response


data class Response<T>(
    val status: Int,
    val message: String? = null,
    val data: T? = null
)
