package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

class CinematographicContentService(val repository: CinematographicContentRepository ) {

    @Transactional
    fun findById(titleId: String): Optional<CinematographicContent> {
        return repository.findById(titleId)
    }

    @Transactional
    open fun add(cinematographicContent: CinematographicContent): CinematographicContent {
        return repository.save(cinematographicContent)
    }


}