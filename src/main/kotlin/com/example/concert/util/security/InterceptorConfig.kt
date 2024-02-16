package com.example.concert.util.security

import com.example.concert.util.redis.*
import com.example.concert.util.security.interceptor.GetSeatStatusInterceptor
import com.example.concert.util.security.interceptor.ReservationRegisterInterceptor
import org.redisson.api.RedissonClient
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig (
    private val redissonClient: RedissonClient
): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(GetSeatStatusInterceptor(QueueWithRedisService(redissonClient)))
            .addPathPatterns("/concert/seat/**")

        registry.addInterceptor(ReservationRegisterInterceptor(QueueWithRedisService( redissonClient)))
            .addPathPatterns("/reservation/register/**")
    }
}