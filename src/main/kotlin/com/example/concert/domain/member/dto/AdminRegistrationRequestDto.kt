package com.example.concert.domain.member.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class AdminRegistrationRequestDto(

    @field:Pattern(regexp = "^[a-z0-9]{4,10}$", message = "Invalid username")
    val memberName: String,


    @field:Size(min = 4, max = 12, message = "Password must be between 4 and 10")
    @field:Pattern(regexp = "^[a-zA-Z0-9!@#\$%^&*]+$", message = "Invalid password")
    val password: String,

    val adminCode: String
)
