package com.example.concert.util.security.interceptor

import com.example.concert.util.redis.QueueWithRedisService
import com.example.concert.util.security.userdetails.UserDetailsImpl
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import java.lang.Exception

class ReservationRegisterInterceptor(
    private val queueWithRedisService: QueueWithRedisService
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

        queueWithRedisService.leaveWorkingQueue(scheduleId, memberId)
        queueWithRedisService.processQueue(scheduleId)

        super.afterCompletion(request, response, handler, ex)
    }
}