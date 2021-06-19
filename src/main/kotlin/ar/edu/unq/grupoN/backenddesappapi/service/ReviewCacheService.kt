package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.persistence.PerformedContentRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PerformedContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewCacheService {
    @Autowired
    private lateinit var performedContentRepository: PerformedContentRepository

    //Instantiated to setup Redis cache config
    @Autowired
    private lateinit var cacheManager: CacheManager

    @Transactional
    @Cacheable(key = "#titleId", value = ["fast-content"])
    fun performedSearchFor(titleId: String): PerformedContent {
        return performedContentRepository.findById(titleId).get()
    }
}