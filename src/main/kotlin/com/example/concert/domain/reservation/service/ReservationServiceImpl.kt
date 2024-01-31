package com.example.concert.domain.reservation.service

import com.example.concert.domain.concert.repository.ConcertRepository
import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.domain.reservation.dto.request.ReservationRequestDto
import com.example.concert.domain.reservation.dto.response.ReservationResponseDto
import com.example.concert.domain.reservation.model.Reservation
import com.example.concert.domain.reservation.repository.ReservationRepository
import com.example.concert.domain.concert.model.Schedule
import com.example.concert.domain.concert.repository.ScheduleRepository
import com.example.concert.domain.concert.model.Seat
import com.example.concert.domain.concert.repository.SeatRepository
import com.example.concert.exception.AuthenticationFailureExceptions
import com.example.concert.exception.DuplicateException
import com.example.concert.exception.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class ReservationServiceImpl(
    private val concertRepository: ConcertRepository,
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository,
    private val memberRepository: MemberRepository,
    private val scheduleRepository: ScheduleRepository
) : ReservationService {

    @Transactional
    override fun makeReservation(reservationRequestDto: ReservationRequestDto, memberName: String)
            : ReservationResponseDto {
        val seatNumList = reservationRequestDto.seatNum
        val scheduleId = reservationRequestDto.scheduleId

        val schedule = scheduleRepository.findById(scheduleId).orElseThrow {
            throw NotFoundException("No Schedule was found for the provided id")
        }

        val member = memberRepository.findByMemberName(memberName).orElseThrow {
            throw NotFoundException("No Member was found for the provided memberName")
        }

        val seatStatus = checkSeatHasReservation(seatNumList, schedule)

        if (seatStatus.containsValue(true)) {
            val bookedSeatList = getAlreadyBookedSeatNum(seatStatus)
            throw DuplicateException("The seat numbers $bookedSeatList have already been booked ")
        }

        val requestSeatList: MutableList<Seat> = mutableListOf()


        seatNumList.forEach {
            val seatList = schedule.seat
                ?: throw (NotFoundException("There is no seat related to the schedule. Seat number ${it}"))
            requestSeatList.add(seatList[it])
        }

        val reservation = Reservation(
            member = member,
            schedule = schedule,
            seatList = requestSeatList,
            reservationDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
        )

        reservationRepository.save(reservation)

        countExtraSeatsAndSave(requestSeatList, schedule)

        return reservationToDtoConverter(reservation)


    }

    @Transactional
    override fun getReservationList(memberName: String): MutableList<ReservationResponseDto> {
        val reservationList: MutableList<ReservationResponseDto> = mutableListOf()

        reservationRepository.findAllByMember_MemberName(memberName).forEach { reservation ->

            reservationList.add(reservationToDtoConverter(reservation))
        }

        return reservationList
    }

    @Transactional
    override fun getReservation(reservationId: UUID, memberName: String): ReservationResponseDto {
        val reservation = reservationRepository
            .findById(reservationId)
            .orElseThrow { NotFoundException("No Reservation was found for id") }

        if(memberName.equals(reservation.member.memberName)){
            return reservationToDtoConverter(reservation)
        }else{
            throw AuthenticationFailureExceptions("The user who booked the concert is different from the currently authorized user.")
        }


    }


    private fun checkSeatHasReservation(seatNumList: MutableList<Int>, schedule: Schedule): MutableMap<Int, Boolean> {
        val seat = schedule.seat ?: throw (NotFoundException("There is no seat that is related to the schedule"))

        val seatStatusMap: MutableMap<Int, Boolean> = mutableMapOf()
        seatNumList.forEach { seatNum ->
            if (!seat[seatNum].isBooked) {
                seatStatusMap.put(seatNum, false)
            } else {
                seatStatusMap.put(seatNum, true)
            }

        }
        return seatStatusMap
    }

    private fun getAlreadyBookedSeatNum(seatStatus: MutableMap<Int, Boolean>): MutableList<Int> {

        val bookedSeatList: MutableList<Int> = mutableListOf()

        seatStatus.forEach { it ->
            if (seatStatus.getValue(it.key)) {
                bookedSeatList.add(it.key)
            }
        }
        return bookedSeatList
    }

    private fun reservationToDtoConverter(reservation: Reservation): ReservationResponseDto {
        val seatNumList: MutableList<Int> = mutableListOf()
        reservation.seatList.forEach { it ->
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
            reservationId = reservation.id ?: throw (NotFoundException("No reservation_id was found for the provided Reservation")),
            memberName = member.memberName,
            concertName = concert.concertName,
            concertDate = schedule.concertDate,
            ticketNum = seatNumList.size,
            seatNum = seatNumList,
            totalPrice = concert.ticketPrice * seatNumList.size,
            reservationDate = reservation.reservationDate
        )
    }

    private fun countExtraSeatsAndSave(requestSeatList: MutableList<Seat>, schedule: Schedule) {

        requestSeatList.forEach { seat ->
            seat.isBooked = true
            seatRepository.save(seat)
        }

        val seatList = schedule.seat ?: throw (NotFoundException("There are no seat list related to the schedule"))

        schedule.extraSeatCount = seatList.count { seat -> !seat.isBooked }

        scheduleRepository.save(schedule)

    }

}