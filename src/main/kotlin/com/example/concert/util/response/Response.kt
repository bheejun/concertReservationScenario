package com.example.concert.util.response

import org.springframework.http.HttpStatus

data class Response<T>(
    val status: Int,
    val message: String? = null,
    val data: T? = null
)
