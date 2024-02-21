package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.request.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class ConcertServiceImpl(
    private val concertRepository: ConcertRepository,
    private val scheduleRepository: ScheduleRepository,
    private val scheduleService: ScheduleService
) : ConcertService {

    @Transactional
    override fun registrationConcert(concertRegistrationRequestDto: ConcertRegistrationRequestDto): ConcertResponseDto {

        val concert = Concert(
            concertName = concertRegistrationRequestDto.concertName,
            artist = concertRegistrationRequestDto.artist,
            ticketPrice = concertRegistrationRequestDto.ticketPrice,
        )
        concertRepository.save(concert)

        scheduleService.makeSchedule(concertRegistrationRequestDto.concertSchedule, concert)


        return concertToDtoConverter(concert)

    }

//    override fun updateConcert(concertId: UUID, concertUpdateRequestDto: ConcertUpdateRequestDto): ConcertUpdateResponseDto {
//        val originalConcert = concertRepository.findById(concertId).orElseThrow {
//            throw NotFoundException("The requested resource(concert) cannot found")
//        }
//
//        concertUpdateRequestDto.concertName?.let { originalConcert.concertName = it }
//        concertUpdateRequestDto.artist?.let { originalConcert.artist = it }
//        concertUpdateRequestDto.concertDate?.let { originalConcert.concertDate = convertStringToLocalDateTime(it) }
//        concertUpdateRequestDto.ticketPrice?.let { originalConcert.ticketPrice = it }
//
//
//        concertRepository.save(originalConcert)
//
//        return ConcertUpdateResponseDto(
//            updatedConcertName = originalConcert.concertName,
//            updatedArtistName = originalConcert.artist,
//            updatedConcertDate = originalConcert.schedule.concertDate,
//            updatedConcertPrice = originalConcert.ticketPrice
//        )
//
//    }

    override fun deleteConcert(concertId: UUID): String {
        concertRepository.deleteById(concertId)
        return "Delete complete"
    }

    @Transactional
    override fun getConcert(concertId: UUID): ConcertResponseDto {
        val concert = concertRepository.findById(concertId).orElseThrow {
            throw NotFoundException("No Concert was found for the provided id")
        }
        return concertToDtoConverter(concert)
    }

    @Transactional
    override fun getConcertListAfterCurrentTIme(pageable: Pageable): MutableList<ConcertResponseDto> {
        val currentDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()

        val scheduleListAfterCurrentTime =
            scheduleRepository.findAllByConcertDateGreaterThan(currentDateTime, pageable).content

        if (scheduleListAfterCurrentTime.isEmpty()) {
            throw NotFoundException("The requested page has no schedule")
        }

        val concertIdList: MutableList<UUID> = mutableListOf()

        scheduleListAfterCurrentTime.forEach { schedule ->
            val concertId =
                schedule.concert.id ?: throw (NotFoundException("The concertId was not found for provided schedule"))
            concertIdList.add(concertId)
        }

        val concertList: MutableList<Concert> = mutableListOf()
        concertIdList.distinct().forEach {
            val concert = concertRepository.findById(it).orElseThrow {
                NotFoundException("The Concert was not found for provided id")
            }
            concertList.add(concert)
        }

        val dtoList :MutableList<ConcertResponseDto> = mutableListOf()

        concertList.forEach {
            dtoList.add(concertToDtoConverter(it))
        }

        return dtoList




    }
    override fun concertToDtoConverter(concert: Concert) : ConcertResponseDto {
        val concertId = concert.id ?: throw NotFoundException("The scheduleList was Not found for provided concert id")
        val scheduleList = scheduleRepository.findAllByConcertId(concertId)
        val dateList : MutableList<LocalDateTime> = mutableListOf()
        val scheduleIdList :MutableList<UUID> = mutableListOf()

        scheduleList.forEach {
            dateList.add(it.concertDate)
            scheduleIdList.add(it.id ?: throw (NotFoundException ("The schedule id was not found for provided schedule")))
        }

            return ConcertResponseDto(
                concertId = concertId,
                concertName = concert.concertName,
                artist = concert.artist,
                concertDate = dateList,
                scheduleIdList = scheduleIdList,
                ticketPrice = concert.ticketPrice
            )
    }

}



