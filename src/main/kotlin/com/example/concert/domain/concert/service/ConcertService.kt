package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.request.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.dto.response.ConcertSeatInfoResponseDto
import com.example.concert.domain.concert.model.Concert
import org.springframework.data.domain.Page
import java.util.UUID

interface ConcertService {
    fun registrationConcert(concertRegistrationRequestDto: ConcertRegistrationRequestDto) :String

    fun getConcertList() : Page<ConcertResponseDto>

    fun getConcertSeatInfo(concertId : UUID) :ConcertSeatInfoResponseDto
}