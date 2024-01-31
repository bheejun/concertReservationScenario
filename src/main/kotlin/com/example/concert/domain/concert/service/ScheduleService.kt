package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.dto.response.GetAvailableDateResponseDto
import com.example.concert.domain.concert.model.Schedule
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ScheduleService {
    fun makeSchedule(dateList: List<String>, concert : Concert) : MutableList<Schedule>

    fun getAvailableDatesForSpecificConcert(concertId : UUID) : GetAvailableDateResponseDto

    fun getAvailableConcertList(pageable: Pageable) : Page<ConcertResponseDto>
}