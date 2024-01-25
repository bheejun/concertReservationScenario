package com.example.concert.util.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
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
                val username = jwtUtil.getUsernameFromToken(jwtToken)
                val userRole = jwtUtil.getUserRoleFromToken(jwtToken)
                if (username != null && userRole != null) {
                    val authority = listOf(SimpleGrantedAuthority(userRole))
                    val auth = UsernamePasswordAuthenticationToken(username, null, authority)
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }
        }

        filterChain.doFilter(request, response)

    }

}