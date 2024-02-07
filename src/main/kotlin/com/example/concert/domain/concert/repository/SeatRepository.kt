package com.example.concert.domain.concert.repository

import com.example.concert.domain.concert.model.Seat
import com.example.concert.domain.reservation.model.Reservation
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface SeatRepository : JpaRepository<Seat, UUID> {
    fun findAllByReservation(reservation : Reservation) : MutableList<Seat>
    fun findAllByScheduleId(scheduleId : UUID) : List<Seat>
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Seat s where s.id in :seatIdList")
    fun findByIdWithPessimisticLock(seatIdList: List<UUID>) : List<Seat>
}