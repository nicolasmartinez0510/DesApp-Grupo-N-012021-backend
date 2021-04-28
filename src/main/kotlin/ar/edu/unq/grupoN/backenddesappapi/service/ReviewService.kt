package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.ContentInfo
import ar.edu.unq.grupoN.backenddesappapi.model.PublicReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PublicDTO
import org.springframework.transaction.annotation.Transactional


class ReviewService(val repository: ReviewRepository, val contentRepository: CinematographicContentRepository) {

    @Transactional
    fun saveReview(titleId: String, review: Review): Review {
        //TODO: verificaciones
        val content = contentRepository.findById(titleId).get()
        review.cinematographicContent = content

        return repository.save(review)
    }

    @Transactional
    fun addFakeReview(review: Review): Review {
        return repository.save(review)
    }

    fun findAll() = repository.findAll()
}