package com.example.concert.util.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig (
    @Value("\${spring.data.redis.host}")
    val host : String,
    @Value("\${spring.data.redis.port}")
    val port : Int
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
    @Bean
    fun redisTemplate(): RedisTemplate<Any, Any> {
        val template = RedisTemplate<Any, Any>()
        val stringRedisSerializer = StringRedisSerializer()
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        template.connectionFactory = redisConnectionFactory(redissonClient())

        template.keySerializer = stringRedisSerializer
        template.hashKeySerializer = jackson2JsonRedisSerializer

        template.valueSerializer = jackson2JsonRedisSerializer
        template.hashValueSerializer = jackson2JsonRedisSerializer


        return template

    }



}