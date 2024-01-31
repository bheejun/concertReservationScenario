package com.example.concert.domain.concert.repository

import com.example.concert.domain.concert.model.Seat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SeatRepository : JpaRepository<Seat, UUID> {
}