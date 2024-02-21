package com.example.concert.domain.concert.service

import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.dto.response.GetAvailableDateResponseDto

import java.util.*

interface ScheduleService {
    fun makeSchedule(dateList: List<String>, concert : Concert)

    fun getAvailableDatesForSpecificConcert(concertId : UUID) : GetAvailableDateResponseDto

}