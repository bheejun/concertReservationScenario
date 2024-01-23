package com.example.concert.domain.member.dto.request

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class MemberRegistrationRequestDto(

    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "Invalid username")
    val memberName: String,


    @Size(min = 4, max = 10, message = "Password must be between 4 and 10")
    @Pattern(regexp = "^[a-zA-Z0-9!@#\$%^&*]+$", message = "Invalid password")
    val password: String,
)
