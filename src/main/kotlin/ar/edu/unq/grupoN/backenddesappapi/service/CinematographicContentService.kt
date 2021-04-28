package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CinematographicContentService {

    @Autowired
    private lateinit var repository: CinematographicContentRepository

    @Transactional
    fun findById(titleId: String): Optional<CinematographicContent> {
        return repository.findById(titleId)
    }

    @Transactional
    open fun add(cinematographicContent: CinematographicContent): CinematographicContent {
        return repository.save(cinematographicContent)
    }


}