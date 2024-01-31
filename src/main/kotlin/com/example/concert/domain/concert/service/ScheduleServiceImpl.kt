package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.domain.concert.dto.response.GetAvailableDateResponseDto
import com.example.concert.domain.concert.model.Schedule
import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.exception.DuplicateException
import com.example.concert.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class ScheduleServiceImpl(
    private val concertRepository: ConcertRepository,
    private val scheduleRepository: ScheduleRepository,
    private val seatService: SeatService
) : ScheduleService {

    @Transactional
    override fun makeSchedule(dateList: List<String>, concert: Concert): MutableList<Schedule> {
        val scheduleList: MutableList<Schedule> = mutableListOf()

        val availableDateList :MutableList<LocalDateTime> = mutableListOf()

        val unAvailableDateList :MutableList<LocalDateTime> = mutableListOf()


        for (date in dateList) {
            val concertDate = convertStringToLocalDateTime(date)

            if(scheduleRepository.existsByConcertDate(concertDate)){
                unAvailableDateList.add(concertDate)
            }else{
                availableDateList.add(concertDate)
            }

        }

        if(unAvailableDateList.isEmpty()){
            availableDateList.forEach { concertDate ->
                val schedule = Schedule(
                    concertDate = concertDate,
                    seatCount = 50,
                    extraSeatCount = 50,
                    concert = concert
                )
                scheduleRepository.save(schedule)

                schedule.seat = seatService.makeSeat(schedule)

                scheduleList.add(schedule)
            }
        }else{
            throw DuplicateException("Concerts are already scheduled fo ${unAvailableDateList}. Try again accept this days")
            }

        return scheduleList
    }

    @Transactional
    override fun getAvailableDatesForSpecificConcert(concertId: UUID): GetAvailableDateResponseDto {

        val concert= concertRepository.findById(concertId).orElseThrow {
            NotFoundException ("No Concert was found for the provided id")
        }

        val availableConcertDateList : MutableList<LocalDateTime> = mutableListOf()
        val unavailableConcertDateList : MutableList<LocalDateTime> = mutableListOf()

        val scheduleList = scheduleRepository.findAllByConcertId(concertId)


        scheduleList.forEach { schedule ->
            if (schedule.extraSeatCount > 0) {
                availableConcertDateList.add(schedule.concertDate)
            }else if(schedule.extraSeatCount == 0){
                unavailableConcertDateList.add(schedule.concertDate)
            }
        }
        val result = "${concert.concertName}'s available date is $availableConcertDateList, and unavailable date is $unavailableConcertDateList"

        val getAvailableDateResponseDto = GetAvailableDateResponseDto(
            availableDate = availableConcertDateList,
            unavailableDates = unavailableConcertDateList,
            result = result
        )

        return getAvailableDateResponseDto
    }

    @Transactional
    override fun getAvailableConcertList(pageable: Pageable): Page<ConcertResponseDto> {
        val currentDateTime = LocalDateTime.now()

        val paginatedSchedules = scheduleRepository.findAllByConcertDateGreaterThan(currentDateTime, pageable)

        val uniqueConcerts = paginatedSchedules.content
            .map { schedule -> schedule.concert }
            .distinct()

        val concertResponseDtoList = uniqueConcerts.map { concert ->
            val concertDates = concert.schedule
                ?.filter { schedule -> schedule.concertDate.isAfter(currentDateTime) && schedule.extraSeatCount > 0}
                ?.map { schedule -> schedule.concertDate }
                ?.sorted()
                ?.toMutableList()
                ?: mutableListOf()

            ConcertResponseDto(
                concertId = concert.id ?: throw (NotFoundException("No concert_id was found for the provided Concert")),
                concertName = concert.concertName,
                artist = concert.artist,
                concertDate = concertDates,
                ticketPrice = concert.ticketPrice
            )
        }

        val pageImpl = PageImpl(concertResponseDtoList, pageable, paginatedSchedules.totalElements)
        return pageImpl
    }
    private fun convertStringToLocalDateTime(stringDateTime: String): LocalDateTime {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.parse(stringDateTime, dateTimeFormatter)
    }

//    private fun concertTODtoConverter(concert: Concert) : ConcertResponseDto{
//
//    }


}