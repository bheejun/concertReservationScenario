package com.example.concert.integrationtest.service

import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.service.ReservationServiceImpl
import com.example.concert.exception.NotFoundException
import com.example.concert.integrationtest.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


class ReservationTest : IntegrationTest() {

    @Autowired
    lateinit var reservationServiceImpl: ReservationServiceImpl

    @Autowired
    lateinit var scheduleRepository: ScheduleRepository

    @Test
    fun reservationConcurrencyTest() {
        val scheduleId = UUID.fromString("5311f80a-9187-414e-b3eb-484d639908bc")
        val seatId: List<UUID> = listOf(UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af75"))


        val reservationRequestDto = ReservationRequestDto(
            seatIdList = seatId
        )

        val numberOfThreads = 11

        val executor = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)


        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af03"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af13"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af23"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af33"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af43"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af53"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af63"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af73"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af83"))
        }
        executor.execute {
            latch.countDown()
            reservationServiceImpl.makeReservation(reservationRequestDto, UUID.fromString("f8eff4fb-2ed4-466e-b606-1968f300af93"))
        }

        latch.countDown()
        latch.await()

        val resultSchedule = scheduleRepository.findById(scheduleId).orElseThrow { NotFoundException("왜없지") }
        assertThat(resultSchedule.extraSeatCount).isEqualTo(49)


    }
}