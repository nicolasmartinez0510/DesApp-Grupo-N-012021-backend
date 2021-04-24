package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import org.springframework.transaction.annotation.Transactional

class CinematographicContentService constructor(private val repository: CinematographicContentRepository )
    {
    @Transactional
    fun add(cinematographicContent: CinematographicContent): CinematographicContent {
        return repository.save(cinematographicContent)
    }
}