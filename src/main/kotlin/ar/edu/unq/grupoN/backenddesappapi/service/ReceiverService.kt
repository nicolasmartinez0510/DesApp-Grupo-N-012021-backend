package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.aspect.ApplicationAuditAspect
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.slf4j.LoggerFactory


@Service
class ReceiverService: MessageListener {
    @Autowired
    protected lateinit var contentRepository: CinematographicContentRepository

    @Autowired
    protected lateinit var restTemplate: RestTemplate

    private val logger = LoggerFactory.getLogger(ReceiverService::class.java)

    @Transactional
    override fun onMessage(message: Message, pattern: ByteArray?) {
        Thread.sleep(4000)
        val titleId = message.toString().substring(7)
        contentRepository
            .findById(titleId)
            .get()
            .subscribers
            .forEach {
                val content = restTemplate.getForObject(it.url, String::class.java)
                logger.info(content)
            }
    }
}