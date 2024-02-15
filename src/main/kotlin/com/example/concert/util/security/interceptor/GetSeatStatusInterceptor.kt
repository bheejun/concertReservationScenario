package com.example.concert.util.security.interceptor

import com.example.concert.util.redis.QueueWithRedisService
import com.example.concert.util.security.userdetails.UserDetailsImpl
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping

class GetSeatStatusInterceptor(
    private val queueWithRedisService: QueueWithRedisService
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest, response: HttpServletResponse, handler: Any
    ): Boolean {
        val pathVariable = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<*, *>
        val scheduleId = pathVariable["scheduleId"] as String
        val authentication = SecurityContextHolder.getContext().authentication
        val memberId: String?

        if (authentication.principal is UserDetailsImpl) {
            memberId = (authentication.principal as UserDetailsImpl).getMemberId().toString()
        } else {
            return false
        }


        queueWithRedisService.addQueueFilter(scheduleId, memberId)


        return true
    }

}