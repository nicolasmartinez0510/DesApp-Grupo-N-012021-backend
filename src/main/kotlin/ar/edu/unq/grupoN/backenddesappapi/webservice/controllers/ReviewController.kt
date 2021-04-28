package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.CinematographicContentService
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ValorationDTO
import org.springframework.web.bind.annotation.*

@ServiceREST
@RequestMapping("/api/review")
class ReviewController(reviewRepository : ReviewRepository, contentRepository: CinematographicContentRepository) {
    private var reviewService: ReviewService = ReviewService(reviewRepository, contentRepository)
    private var contentService: CinematographicContentService = CinematographicContentService(contentRepository)

    @PostMapping("/add")
    fun addReview(@RequestBody createReviewRequest: CreateReviewRequest): ReviewDTO {
        val myReview = createReviewRequest.reviewToCreate.toModel()
        reviewService.saveReview(createReviewRequest.titleId, myReview)

        return ReviewDTO.fromModel(myReview)
    }

    @GetMapping
    fun reviews(): List<ReviewDTO> {
        return reviewService.findAll().map { review -> ReviewDTO.fromModel(review) }
    }
}

data class CreateReviewRequest(val titleId: String, val reviewToCreate: ReviewDTO)