package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.model.Schedule
import com.example.concert.domain.concert.dto.response.SeatStatusResponseDto
import com.example.concert.domain.concert.model.Seat

interface SeatService {
    fun makeSeat(schedule : Schedule) : MutableList<Seat>
    fun getSeatStatus() : SeatStatusResponseDto
}