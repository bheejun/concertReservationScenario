package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.model.Schedule
import com.example.concert.domain.concert.dto.response.SeatStatusResponseDto
import com.example.concert.domain.concert.model.Seat
import com.example.concert.domain.concert.repository.SeatRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

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
    override fun getSeatStatus(): SeatStatusResponseDto {

        return SeatStatusResponseDto(
            seatNum = 1
        )
    }

}