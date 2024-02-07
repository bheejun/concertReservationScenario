package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.domain.concert.dto.response.GetAvailableDateResponseDto
import com.example.concert.domain.concert.model.Schedule
import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.exception.DuplicateException
import com.example.concert.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class ScheduleServiceImpl(
    private val concertRepository: ConcertRepository,
    private val scheduleRepository: ScheduleRepository
) : ScheduleService {

    @Transactional
    override fun makeSchedule(dateList: List<String>, concert: Concert){
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
            }
        }else{
            throw DuplicateException("Concerts are already scheduled for ${unAvailableDateList}. Try again accept this days")
            }
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

//    @Transactional
//    override fun getAvailableConcertList(pageable: Pageable): MutableList<ConcertResponseDto> {
//        val currentDateTime = LocalDateTime.now()
//
//        val concertListAfterCurrentTIme = concertService.getConcertListAfterCurrentTIme(pageable)
//
//        concertListAfterCurrentTIme.forEach { it ->
//            it.scheduleIdList.forEach { scheduleId ->
//                scheduleRepository.findById(scheduleId)
//            }
//        }
//
//            ConcertResponseDto(
//                concertId = concert.id ?: throw (NotFoundException("No concert_id was found for the provided Concert")),
//                concertName = concert.concertName,
//                artist = concert.artist,
//                concertDate = concertDates,
//                ticketPrice = concert.ticketPrice,
//                scheduleIdList = scheduleIdList
//            )
//        }
//
//    }
    private fun convertStringToLocalDateTime(stringDateTime: String): LocalDateTime {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.parse(stringDateTime, dateTimeFormatter)
    }



}