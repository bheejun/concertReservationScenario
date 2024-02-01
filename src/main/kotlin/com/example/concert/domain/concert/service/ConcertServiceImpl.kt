package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.request.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
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
            ticketPrice = concertRegistrationRequestDto.ticketPrice
        )
        concertRepository.save(concert)


        val scheduleList = scheduleService.makeSchedule(concertRegistrationRequestDto.concertSchedule, concert)

        concert.schedule = scheduleList

        val concertDateList :MutableList<LocalDateTime> = mutableListOf()
        val scheduleIdList : MutableList<UUID> = mutableListOf()

        concert.schedule?.forEach { it->
            scheduleIdList.add(it.id ?: throw NotFoundException("No schedule_id was found for the provided Schedule"))
        }


        scheduleList.forEach { schedule ->
            concertDateList.add(schedule.concertDate)
        }

        return ConcertResponseDto(
            concertId = concert.id ?: throw (NotFoundException("No concert_id was found for the provided Concert")),
            concertName = concert.concertName,
            artist = concert.concertName,
            ticketPrice = concert.ticketPrice,
            concertDate = concertDateList,
            scheduleId = scheduleIdList
        )

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

        val concertSchedule: MutableList<LocalDateTime> = mutableListOf()
        scheduleRepository.findAllByConcert(concert).forEach { schedule ->
            concertSchedule.add(schedule.concertDate)
        }

        val scheduleIdList : MutableList<UUID> = mutableListOf()
        concert.schedule?.forEach {
            scheduleIdList.add(it.id ?: throw NotFoundException("No schedule_id was found for the provided Schedule"))
        }

        return ConcertResponseDto(
            concertId = concertId,
            concertName = concert.concertName,
            artist = concert.artist,
            concertDate = concertSchedule,
            ticketPrice = concert.ticketPrice,
            scheduleId = scheduleIdList
        )
    }

    @Transactional
    override fun getConcertListAfterCurrentTIme(pageable: Pageable): Page<ConcertResponseDto> {
        val currentDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()

        val paginatedSchedules = scheduleRepository.findAllByConcertDateGreaterThan(currentDateTime, pageable)

        val uniqueConcerts = paginatedSchedules.content
            .map { schedule -> schedule.concert }
            .distinct()

        val concertResponseDtoList = uniqueConcerts.map { concert ->
            val concertDates = concert.schedule
                ?.filter { schedule -> schedule.concertDate.isAfter(currentDateTime) }
                ?.map { schedule -> schedule.concertDate }
                ?.sorted()
                ?.toMutableList()
                ?: mutableListOf()

            val scheduleIdList : MutableList<UUID> = mutableListOf()
            concert.schedule?.forEach {
                scheduleIdList.add(it.id ?: throw NotFoundException("No schedule_id was found for the provided Schedule"))
            }

            ConcertResponseDto(
                concertId = concert.id ?: throw (NotFoundException("No concert_id was found for the provided Concert")),
                concertName = concert.concertName,
                artist = concert.artist,
                concertDate = concertDates,
                ticketPrice = concert.ticketPrice,
                scheduleId = scheduleIdList
            )
        }

        val pageImpl = PageImpl(concertResponseDtoList, pageable, paginatedSchedules.totalElements)
        return pageImpl

    }

}