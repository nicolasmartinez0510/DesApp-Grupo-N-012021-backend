package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import org.springframework.data.repository.CrudRepository

interface ReviewRepository: CrudRepository<Review, Long?>