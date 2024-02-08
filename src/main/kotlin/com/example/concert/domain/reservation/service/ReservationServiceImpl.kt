package com.example.concert.domain.reservation.service

import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.dto.response.ReservationResponseDto
import com.example.concert.domain.reservation.model.Reservation
import com.example.concert.domain.reservation.repository.ReservationRepository
import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.domain.concert.model.Seat
import com.example.concert.domain.concert.repository.SeatRepository
import com.example.concert.domain.member.model.Member
import com.example.concert.exception.AlreadyCanceledReservationException
import com.example.concert.exception.AuthenticationFailureException
import com.example.concert.exception.NotFoundException
import com.example.concert.util.event.ReservationCompleteEvent
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class ReservationServiceImpl(
    private val concertRepository: ConcertRepository,
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository,
    private val scheduleRepository: ScheduleRepository,
    private val eventPublisher: ApplicationEventPublisher
) : ReservationService {

    @Transactional
    override fun makeReservation(
        reservationRequestDto: ReservationRequestDto,
        member: Member
    ): ReservationResponseDto {

        val requestSeatList = seatRepository.findByIdWithPessimisticLock(reservationRequestDto.seatIdList)

        val schedule = requestSeatList[0].schedule


        val reservation = Reservation(
            member = member,
            schedule = schedule,
            reservationDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
        )

        val savedReservation = reservationRepository.save(reservation)
        completeReservation(requestSeatList, savedReservation)

        eventPublisher
            .publishEvent(
                ReservationCompleteEvent(
                    this,
                    member.id ?: throw NotFoundException("The member id was not found for provided member"),
                    schedule.id ?: throw NotFoundException("The schedule id was not found for provided schedule")
                )
            )
        return reservationToDtoConverter(reservation)
    }

    fun completeReservation(
        requestSeatList: List<Seat>,
        savedReservation: Reservation
    ) {
        for (seat in requestSeatList) {
            val schedule = seat.schedule
            seat.booking()
            seat.reservation = savedReservation
            schedule.extraSeatCount -= 1
        }
    }

    @Transactional
    override fun getReservationList(member: Member): MutableList<ReservationResponseDto> {
        val reservationList: MutableList<ReservationResponseDto> = mutableListOf()

        reservationRepository.findAllByMember(member).forEach { reservation ->
            if (!reservation.cancelStatus) {
                reservationList.add(reservationToDtoConverter(reservation))
            }
        }

        return reservationList
    }

    @Transactional
    override fun getReservation(reservationId: UUID, memberName: String): ReservationResponseDto {
        val reservation = reservationRepository
            .findById(reservationId)
            .orElseThrow { NotFoundException("No Reservation was found for id") }

        if (memberName == reservation.member.memberName) {
            return reservationToDtoConverter(reservation)
        } else {
            throw AuthenticationFailureException("The user who booked the concert is different from the currently authorized user.")
        }


    }

    @Transactional
    override fun cancelReservation(memberName: String, reservationId: UUID): String {
        val reservation = reservationRepository.findById(reservationId).orElseThrow {
            NotFoundException("No Reservation was found for id")
        }

        if (memberName != reservation.member.memberName) {
            throw AuthenticationFailureException("The user who booked the concert is different from the currently authorized user.")
        }

        if (!reservation.cancelStatus) {
            reservation.cancelStatus = true

            val reservationSeatList = seatRepository.findAllByReservation(reservation)
            val updatedSeatList: MutableList<Seat> = mutableListOf()

            reservationSeatList.forEach { seat ->
                seat.reservation = null
                seat.isBooked = false

                updatedSeatList.add(seatRepository.save(seat))
            }


            val schedule = reservation.schedule

            schedule.extraSeatCount += updatedSeatList.size


            reservationRepository.save(reservation)
            scheduleRepository.save(schedule)

            return "Reservation cancel complete"
        } else {
            throw AlreadyCanceledReservationException("This reservation was already canceled")
        }
    }


    private fun reservationToDtoConverter(reservation: Reservation): ReservationResponseDto {
        val seatNumList: MutableList<Int> = mutableListOf()
        val reservationSeatsList = seatRepository.findAllByReservation(reservation)
        reservationSeatsList.forEach {
            seatNumList.add(it.seatNum)
        }
        val member = reservation.member

        val schedule = reservation.schedule

        val concert = concertRepository
            .findById(
                schedule.concert.id ?: throw NotFoundException("There is no concert that is related to the schedule")
            )
            .orElseThrow { throw NotFoundException("No Concert was found for the provided id") }


        return ReservationResponseDto(
            reservationId = reservation.id
                ?: throw (NotFoundException("No reservation_id was found for the provided Reservation")),
            memberName = member.memberName,
            concertName = concert.concertName,
            concertDate = schedule.concertDate,
            ticketNum = seatNumList.size,
            seatNum = seatNumList,
            totalPrice = concert.ticketPrice * seatNumList.size,
            reservationDate = reservation.reservationDate
        )
    }

}