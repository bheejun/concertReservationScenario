package com.example.concert.util.security

import com.example.concert.util.jwt.JwtFilter
import com.example.concert.util.jwt.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtUtil: JwtUtil) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http

            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/member/**","/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                    .requestMatchers("/concert/register").hasAuthority("ADMIN")
                    .anyRequest().authenticated()

            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .headers { headers ->
                headers.frameOptions { frameOptions ->
                    frameOptions.disable()
                }
            }
            .exceptionHandling { it.accessDeniedHandler(accessDeniedHandler()) }

        return http.build()!!
    }




    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun jwtFilter(): JwtFilter {
        return JwtFilter(jwtUtil)
    }

    @Bean
    fun accessDeniedHandler() = CustomAccessDeniedHandler()
}