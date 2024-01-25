package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.request.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.dto.response.ConcertSeatInfoResponseDto
import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class ConcertServiceImpl(private val concertRepository: ConcertRepository) : ConcertService {

    @Transactional
    override fun registrationConcert(concertRegistrationRequestDto: ConcertRegistrationRequestDto): String {
        val stringConcertDateList = concertRegistrationRequestDto.concertDate
        val dateTimeConcertDateList :MutableList<LocalDateTime> = mutableListOf()

        stringConcertDateList.forEach { stringConcertDate ->
            dateTimeConcertDateList.add(convertStringToLocalDateTime(stringConcertDate))
        }


        val unavailableDate = dateTimeConcertDateList.filter { concertRepository.existsByConcertDate(it) }


        if (unavailableDate.isEmpty()) {
            dateTimeConcertDateList.forEach { concertDate ->
                concertRepository.save(
                    Concert(
                        artist = concertRegistrationRequestDto.artist,
                        concertName = concertRegistrationRequestDto.concertName,
                        concertDate = concertDate,
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

    override fun getConcertList(): Page<ConcertResponseDto> {
        val pageable = PageRequest.of(0, 5, Sort.by("concertDate"))

        val concertPage :Page<Concert> = concertRepository.findAllByConcertDateGreaterThan(LocalDateTime.now(), pageable )

        return concertPage.map { concert ->
            ConcertResponseDto(
                concertName = concert.concertName,
                concertDate = concert.concertDate,
                ticketPrice = concert.ticketPrice,
                artist = concert.artist
            )
        }
    }

    override fun getConcertSeatInfo(concertId: UUID): ConcertSeatInfoResponseDto {
        val concertSeatInfo = concertRepository.findById(concertId).orElseThrow{
            throw NotFoundException("Cannot found concert that match with id")
        }
        return ConcertSeatInfoResponseDto(
            seatInfo = concertSeatInfo.seat
        )
    }

    private fun convertStringToLocalDateTime(stringDateTime: String) : LocalDateTime{
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.parse(stringDateTime, dateTimeFormatter)
    }
}