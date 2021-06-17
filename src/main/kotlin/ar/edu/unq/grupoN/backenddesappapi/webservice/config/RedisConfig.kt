package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import ar.edu.unq.grupoN.backenddesappapi.service.ReceiverService
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.web.client.RestTemplate


@Configuration
class RedisConfig {

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val env = System.getenv()
        val redisStandaloneConfiguration =
            RedisStandaloneConfiguration(
                env.getOrDefault("REDIS_URL", "localhost"),
                6379
            )
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun redisTemplate(lettuceConnectionFactory: LettuceConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(lettuceConnectionFactory)
        return template
    }

    @Bean
    fun topic(): ChannelTopic = ChannelTopic("pubsub:review-channel")

    @Bean
    fun receiver(): ReceiverService = ReceiverService()

    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder().build()

    @Bean
    fun messageListenerAdapter(receiverService: ReceiverService): MessageListenerAdapter =
        MessageListenerAdapter(receiverService, "onMessage")

    @Bean
    fun redisMessageContainer(
        lettuceConnectionFactory: LettuceConnectionFactory,
        messageListenerAdapter: MessageListenerAdapter,
        channelTopic: ChannelTopic,
        receiverService: ReceiverService
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(lettuceConnectionFactory)
        container.addMessageListener(messageListenerAdapter(receiverService), channelTopic)
        return container
    }

}
