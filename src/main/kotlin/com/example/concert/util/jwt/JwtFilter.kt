package com.example.concert.util.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter (private val jwtUtil: JwtUtil) : OncePerRequestFilter(){

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain){

        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            val jwtToken = bearerToken.substring(7)
            if (jwtUtil.validateToken(jwtToken)) {
                val username = jwtUtil.getUsernameFromToken(jwtToken) ?: throw NullPointerException("The username is null.")

                val authentication = jwtUtil.createAuthentication(username)

                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)

    }

}