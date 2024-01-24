package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.ConcertRegistrationRequestDto

interface ConcertService {
    fun registrationConcert(concertRegistrationRequestDto: ConcertRegistrationRequestDto) :String
}