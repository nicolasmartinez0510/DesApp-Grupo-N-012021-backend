package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import org.springframework.transaction.annotation.Transactional

open class CinematographicContentService constructor(private val repository: CinematographicContentRepository )
    {
    @Transactional
    open fun add(cinematographicContent: CinematographicContent): CinematographicContent {
        return repository.save(cinematographicContent)
    }
}