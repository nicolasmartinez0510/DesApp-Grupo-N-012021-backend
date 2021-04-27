package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import org.springframework.data.repository.CrudRepository




interface CinematographicContentRepository : CrudRepository<CinematographicContent, String?> {
}