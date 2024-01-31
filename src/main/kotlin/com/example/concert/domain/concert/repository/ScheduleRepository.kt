package com.example.concert.domain.concert.repository

import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.concert.model.Schedule
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface ScheduleRepository: JpaRepository<Schedule, UUID> {

    fun existsByConcertDate(concertDate : LocalDateTime) : Boolean
    fun findAllByConcertDateGreaterThan(currentDateTime : LocalDateTime, pageable: Pageable) : Page<Schedule>

    fun findAllByConcert(concert: Concert) : List<Schedule>

    fun findAllByConcertId(concertId : UUID) : List<Schedule>


}