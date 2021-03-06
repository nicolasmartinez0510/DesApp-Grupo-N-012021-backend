package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.PerformedContentRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PerformedContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CinematographicContentService {

    @Autowired
    private lateinit var repository: CinematographicContentRepository

    @Transactional
    fun add(cinematographicContent: CinematographicContent): CinematographicContent {
        return repository.save(cinematographicContent)
    }
}