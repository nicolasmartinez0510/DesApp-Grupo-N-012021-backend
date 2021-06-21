package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate


@Bean
fun jedisConnectionFactory(): JedisConnectionFactory {
    val redisStandaloneConfiguration =
        RedisStandaloneConfiguration(
            System.getenv().getOrDefault("REDIS_URL", "localhost"),
            6379
        )
    return JedisConnectionFactory(redisStandaloneConfiguration)
}

@Bean
fun redisTemplate(): RedisTemplate<String, Any> {
    val template = RedisTemplate<String, Any>()
    template.setConnectionFactory(jedisConnectionFactory())
    return template
}