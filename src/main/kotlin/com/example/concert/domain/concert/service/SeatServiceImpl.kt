package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.model.Schedule
import com.example.concert.domain.concert.dto.response.SeatStatusResponseDto
import com.example.concert.domain.concert.model.Seat
import com.example.concert.domain.concert.repository.SeatRepository
import com.example.concert.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SeatServiceImpl(private val seatRepository: SeatRepository) : SeatService {

    @Transactional
    override fun makeSeat(schedule: Schedule): MutableList<Seat> {
        val seatList: MutableList<Seat> = mutableListOf()

        for (i in 1 until 51) {
            val seat = Seat(
                seatNum = i,
                schedule = schedule,
                isBooked = false
                )
            seatRepository.save(seat)
            seatList.add(seat)

        }
        return seatList

    }



    @Transactional
    override fun getSeatsStatus(scheduleId : UUID): MutableList<SeatStatusResponseDto> {
        val dtoList : MutableList<SeatStatusResponseDto> = mutableListOf()
        seatRepository.findAllByScheduleId(scheduleId).forEach {
            dtoList.add(SeatStatusResponseDto(
                seatId = it.id ?: throw NotFoundException("The seat id was not found for provided Seat"),
                seatNum = it.seatNum,
                isBooked = it.isBooked))
        }

        return dtoList

    }

}