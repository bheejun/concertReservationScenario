package com.example.concert.domain.concert.repository

import com.example.concert.domain.concert.model.Concert
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface ConcertRepository : JpaRepository<Concert, UUID> {

    fun existsByConcertDate(concertDate : LocalDateTime) :Boolean
    fun findAllByConcertDateGreaterThan (concertDate: LocalDateTime, pageable: Pageable) :Page<Concert>

}