package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.Duration
import java.time.temporal.ChronoUnit


@Configuration
class CacheConfig : CachingConfigurerSupport() {

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory?): CacheManager {
        val redisCacheConfigurationMap: MutableMap<String, RedisCacheConfiguration> = HashMap()
        redisCacheConfigurationMap[CONTENT_CACHE] = createConfig(30, ChronoUnit.SECONDS)
        return RedisCacheManager
            .builder(redisConnectionFactory!!)
            .withInitialCacheConfigurations(redisCacheConfigurationMap)
            .build()
    }

    companion object {
        const val CONTENT_CACHE = "fast-content"
        private fun createConfig(time: Long, temporalUnit: ChronoUnit): RedisCacheConfiguration {
            return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.of(time, temporalUnit))
        }
    }
}