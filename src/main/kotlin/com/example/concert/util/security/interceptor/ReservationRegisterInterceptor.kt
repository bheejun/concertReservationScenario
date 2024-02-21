package com.example.concert.util.security.interceptor

import com.example.concert.domain.payment.PaymentService
import com.example.concert.domain.redis.QueueWithRedisService
import com.example.concert.domain.reservation.repository.ReservationRepository
import com.example.concert.exception.NotFoundException
import com.example.concert.util.security.userdetails.UserDetailsImpl
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import java.lang.Exception
import java.util.*

class ReservationRegisterInterceptor(
    private val queueWithRedisService: QueueWithRedisService,
    private val reservationRepository: ReservationRepository,
    private val paymentService: PaymentService
) : HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val pathVariable = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<*,*>
        val scheduleId =pathVariable["scheduleId"] as String
        val memberId = (SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl).getMemberId().toString()
        val reservation = reservationRepository
            .findByMemberIdAndScheduleIdAndCancelStatusIsFalse(UUID.fromString(memberId),UUID.fromString(scheduleId))
            .orElseThrow { NotFoundException ("The Reservation is not found for provided ids") }

        queueWithRedisService.leaveWorkingQueue(scheduleId, memberId)
        queueWithRedisService.processQueue(scheduleId)
        paymentService.startPaymentCheck(memberId, reservation.id.toString())

        super.afterCompletion(request, response, handler, ex)
    }
}