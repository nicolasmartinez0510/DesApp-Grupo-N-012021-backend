package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.ReviewTypeException
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ValorationDTO
import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.CreateReviewRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReviewService{

    @Autowired
    private lateinit var repository: ReviewRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Transactional
    fun saveReview(createReviewRequest: CreateReviewRequest): ReviewDTO {
        val content = contentRepository.findById(createReviewRequest.titleId).get()
        var review = createReviewRequest.reviewToCreate.toModel()
        review.cinematographicContent = content

        review.validate()
        review = repository.save(review)

        return ReviewDTO.fromModel(review)
    }

    @Transactional
    fun rate(reviewId: Long, valorationDTO: ValorationDTO): ReviewDTO {
        val review: Review = repository.findById(reviewId).get()

        review.rate(valorationDTO.userId, valorationDTO.platform, valorationDTO.valoration)

        return ReviewDTO.fromModel(repository.save(review))
    }


    @Transactional
    fun search(titleId: String): List<ReviewDTO> {
        val reviewsResult = repository.findByCinematographicContentTitleIdOrderByValorationDesc(titleId)

        return reviewsResult.map { ReviewDTO.fromModel(it) }
    }

    @Transactional
    fun addFakeReview(review: Review) {
        repository.save(review)
    }

    @Transactional
    fun findAll(): List<ReviewDTO> = repository.findAll().map { ReviewDTO.fromModel(it) }
}
