package com.example.concert.domain.reservation.repository

import com.example.concert.domain.member.model.Member
import com.example.concert.domain.reservation.model.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface ReservationRepository : JpaRepository<Reservation, UUID>{

    fun findAllByMember(member : Member) : List<Reservation>
    fun findByMemberIdAndScheduleIdAndCancelStatusIsFalse(memberId : UUID, scheduleId : UUID) : Optional<Reservation>
}
