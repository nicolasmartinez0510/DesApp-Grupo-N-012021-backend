package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.persistence.PerformedContentRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PerformedContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewCacheService {
    @Autowired
    private lateinit var performedContentRepository: PerformedContentRepository

    @Transactional
    fun saveContentInCache(content: PerformedContent) = performedContentRepository.save(content)

    @Transactional
    fun obtain(titleId: String): PerformedContent {
        val maybeContent = performedContentRepository.findById(titleId)
        return if (maybeContent.isPresent) {
            maybeContent.get()
        } else {
            throw RuntimeException("$titleId is not available.")
        }
    }
}