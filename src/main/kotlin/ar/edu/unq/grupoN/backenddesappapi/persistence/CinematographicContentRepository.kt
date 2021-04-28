package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
@Configuration
interface CinematographicContentRepository : CrudRepository<CinematographicContent, String?> {
}