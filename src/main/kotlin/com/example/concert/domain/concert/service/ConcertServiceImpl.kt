package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.repository.ConcertRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ConcertServiceImpl(private val concertRepository: ConcertRepository) : ConcertService {

    @Transactional
    override fun registrationConcert(concertRegistrationRequestDto: ConcertRegistrationRequestDto): String {
        val concertDateList = concertRegistrationRequestDto.concertDate
        val unavailableDate = concertDateList.filter { concertRepository.existsByDate(it) }

        if (unavailableDate.isEmpty()) {
            concertDateList.forEach { concertDate ->
                concertRepository.save(
                    Concert(
                        artist = concertRegistrationRequestDto.artist,
                        concertName = concertRegistrationRequestDto.concertName,
                        date = concertDate,
                        seat = (1..50).map { it.toString() },
                        ticketPrice = concertRegistrationRequestDto.ticketPrice
                    )
                )
            }
            return "Concert registration complete"
        } else {
            return "$unavailableDate is already booked"
        }
    }
}