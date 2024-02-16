package com.example.concert.util.security.interceptor

import com.example.concert.util.redis.QueueWithRedisService
import com.example.concert.util.security.userdetails.UserDetailsImpl
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import java.lang.Exception

class GetSeatStatusInterceptor(
    private val queueWithRedisService: QueueWithRedisService
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(GetSeatStatusInterceptor::class.java)

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

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val pathVariable = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<*, *>
        val scheduleId = pathVariable["scheduleId"] as String
        val authentication = SecurityContextHolder.getContext().authentication
        val memberId: String?

        if (authentication.principal is UserDetailsImpl) {
            memberId = (authentication.principal as UserDetailsImpl).getMemberId().toString()
        } else {
            return
        }

        try {
            queueWithRedisService.startCheck(scheduleId, memberId)
        } catch (e: Exception) {
            logger.error("afterCompletion error: {}", e.message)
        }

        super.afterCompletion(request, response, handler, ex)
    }

}