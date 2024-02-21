package com.example.concert.domain.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RedisConfig (
    @Value("\${spring.data.redis.host}")
    private val host : String,
    @Value("\${spring.data.redis.port}")
    private val port : String

){
    @Bean(destroyMethod = "shutdown")
    fun redissonClient() : RedissonClient{
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://$host:$port")
            .setDnsMonitoringInterval(-1)
        return Redisson.create(config)
    }

    @Bean
    fun redisConnectionFactory(redissonClient: RedissonClient) : RedissonConnectionFactory{
        return RedissonConnectionFactory(redissonClient)
    }


}