package com.example.concert.integrationtest.service

import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.domain.concert.repository.SeatRepository
import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.repository.ReservationRepository
import com.example.concert.domain.reservation.service.ReservationServiceImpl
import com.example.concert.exception.NotFoundException
import com.example.concert.integrationtest.IntegrationTest
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


class ReservationTest : IntegrationTest() {
//    @Autowired
//    lateinit var reservationService: ReservationService

    @Autowired
    lateinit var reservationServiceImpl: ReservationServiceImpl

    @Autowired
    lateinit var scheduleRepository: ScheduleRepository

    @Autowired
    lateinit var concertRepository: ConcertRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @Autowired
    lateinit var seatRepository: SeatRepository


    @Test
    fun reservationConcurrencyTest() {
        val scheduleId = UUID.fromString("5311f80a-9187-414e-b3eb-484d639908bc")
        val schedule = scheduleRepository.findById(scheduleId).orElseThrow { NotFoundException("왜없지") }
        val seatId: List<UUID> = listOf(UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af75"))


        val reservationRequestDto = ReservationRequestDto(
            seatIdList = seatId
        )



        val numberOfThreads = 11

        val executor = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)


        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member1")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member2")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member3")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member4")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member5")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member6")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member7")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member8")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member9")
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, memberName = "Member10")
        }

        latch.countDown()
        latch.await()

        val resultSchedule = scheduleRepository.findById(scheduleId).orElseThrow { NotFoundException("왜없지") }
        assertThat(resultSchedule.extraSeatCount).isEqualTo(49)


    }
}