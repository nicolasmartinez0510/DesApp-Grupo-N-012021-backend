package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.PerformedContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CinematographicContentService {

    @Autowired
    private lateinit var repository: CinematographicContentRepository

    @Autowired
    private lateinit var performedContentRepository: PerformedContentRepository

    @Transactional
    fun findById(titleId: String): Optional<CinematographicContent> {
        return repository.findById(titleId)
    }

    @Transactional
    fun add(cinematographicContent: CinematographicContent): CinematographicContent {
        performedContentRepository.save(PerformedContent.from(cinematographicContent))
        return repository.save(cinematographicContent)
    }


}