package com.example.concert.domain.concert.repository

import com.example.concert.domain.concert.model.Concert
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ConcertRepository : JpaRepository<Concert, UUID> {

}