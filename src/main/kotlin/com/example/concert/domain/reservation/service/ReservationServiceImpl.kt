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
import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.domain.redis.QueueWithRedisService
import com.example.concert.exception.AlreadyCanceledReservationException
import com.example.concert.exception.AuthenticationFailureException
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
    private val memberRepository: MemberRepository,
    private val seatRepository: SeatRepository,
    private val scheduleRepository: ScheduleRepository,
    private val queueWithRedisService: QueueWithRedisService
) : ReservationService {

    @Transactional
    override fun makeReservation(
        reservationRequestDto: ReservationRequestDto,
        memberId : UUID
    ): ReservationResponseDto {
        val member = memberRepository.findById(memberId).orElseThrow {
            NotFoundException("The Member was not found for provided memberName")
        }

        val requestSeatList = seatRepository.findByIdWithPessimisticLock(reservationRequestDto.seatIdList)

        val schedule = requestSeatList[0].schedule

        val concert = concertRepository
            .findById(
                schedule.concert.id ?: throw NotFoundException("There is no concert that is related to the schedule")
            )
            .orElseThrow { throw NotFoundException("No Concert was found for the provided id") }


        val reservation = Reservation(
            member = member,
            schedule = schedule,
            totalPrice = requestSeatList.size*concert.ticketPrice,
            reservationDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
        )

        val savedReservation = reservationRepository.save(reservation)
        completeReservation(requestSeatList, savedReservation)
        queueWithRedisService.createPaymentToken(reservation.id.toString())

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
                if(queueWithRedisService.checkPaymentTokenExist(reservation.id!!.toString())){
                    reservationList.add(reservationToDtoConverter(reservation))
                }else{
                    cancelReservation(reservation.member.id!!, reservation.id!!)
                }

            }
        }

        return reservationList
    }

    @Transactional
    override fun getReservation(reservationId: UUID, memberId : UUID): ReservationResponseDto {
        val reservation = reservationRepository
            .findById(reservationId)
            .orElseThrow { NotFoundException("No Reservation was found for id") }

        if (memberId == reservation.member.id) {
            return reservationToDtoConverter(reservation)
        } else {
            throw AuthenticationFailureException("The user who booked the concert is different from the currently authorized user.")
        }


    }

    @Transactional
    override fun cancelReservation(memberId : UUID, reservationId: UUID): String {
        val reservation = reservationRepository.findById(reservationId).orElseThrow {
            NotFoundException("No Reservation was found for id")
        }

        if (memberId != reservation.member.id) {
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

        return ReservationResponseDto(
            reservationId = reservation.id
                ?: throw (NotFoundException("No reservation_id was found for the provided Reservation")),
            memberName = member.memberName,
            concertName = schedule.concert.concertName,
            concertDate = schedule.concertDate,
            ticketNum = seatNumList.size,
            seatNum = seatNumList,
            totalPrice = reservation.totalPrice,
            paymentStatus = reservation.payStatus,
            expiredStatus = !queueWithRedisService.checkPaymentTokenExist(reservation.id.toString()),
            reservationDate = reservation.reservationDate
        )
    }




}