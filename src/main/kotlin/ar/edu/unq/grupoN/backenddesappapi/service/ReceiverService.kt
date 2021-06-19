package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
open class ReceiverService: MessageListener {
    @Autowired
    protected lateinit var contentRepository: CinematographicContentRepository

    @Autowired
    protected lateinit var restTemplate: RestTemplate

    @Transactional
    override fun onMessage(message: Message, pattern: ByteArray?) {
        Thread.sleep(4000)
        val titleId = message.toString().substring(7)
        contentRepository
            .findById(titleId)
            .get()
            .subscribers
            .map {
                val content = restTemplate.getForObject(it.url, String::class.java)
                // TODO: CAMBIAR POR LOGGER
                println(content)
            }
    }
}