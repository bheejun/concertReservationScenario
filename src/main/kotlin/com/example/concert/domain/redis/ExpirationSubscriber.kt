package com.example.concert.domain.redis

import jakarta.annotation.PostConstruct
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.springframework.stereotype.Component

@Component
class ExpirationSubscriber(
    private val redissonClient: RedissonClient,
    private val queueWithRedisService: QueueWithRedisService
) {
    @PostConstruct
    fun subscribeToKeyEvents() {

        val expiredTopic = redissonClient.getTopic("__keyevent@0__:expired", StringCodec.INSTANCE)
        expiredTopic.addListener(String::class.java) { _, key->
            if(key.startsWith("jun")){
                handleKeyExpirationOrDeletion(key.toString())
            }
        }

        val deletedTopic = redissonClient.getTopic("__keyevent@0__:del", StringCodec.INSTANCE)
        deletedTopic.addListener(String::class.java) { _, key->
            if(key.startsWith("jun")){
                handleKeyExpirationOrDeletion(key.toString())
            }
        }
    }

    private fun handleKeyExpirationOrDeletion(key: String) {
        val parts= key.split(":")
        val scheduleId = parts[1]
        val memberId = parts[2]

        queueWithRedisService.leaveWorkingQueue(scheduleId, memberId)
        queueWithRedisService.processQueue(scheduleId)
    }
}
