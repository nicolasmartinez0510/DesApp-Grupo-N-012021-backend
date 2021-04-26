package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import org.springframework.transaction.annotation.Transactional

class ReviewService constructor(private val repository: ReviewRepository)
{
    @Transactional
    fun add(review: Review): Review {
        return repository.save(review)
    }
}