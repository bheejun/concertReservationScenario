package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.request.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.model.Concert
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ConcertService {
    fun registrationConcert(concertRegistrationRequestDto: ConcertRegistrationRequestDto) :ConcertResponseDto
//    fun updateConcert(concertId: UUID, concertUpdateRequestDto: ConcertUpdateRequestDto) : ConcertUpdateResponseDto
    fun deleteConcert(concertId: UUID) : String
    fun getConcert(concertId : UUID) : ConcertResponseDto
    fun getConcertListAfterCurrentTIme(pageable: Pageable) : MutableList<ConcertResponseDto>

    fun concertToDtoConverter(concert: Concert) : ConcertResponseDto

}