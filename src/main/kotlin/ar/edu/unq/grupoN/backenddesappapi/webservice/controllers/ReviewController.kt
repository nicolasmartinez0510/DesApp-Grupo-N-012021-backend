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

    @PutMapping("/rate/{reviewId}")
    fun rate(@PathVariable reviewId: Long, @RequestBody valorationDTO: ValorationDTO): ReviewDTO{
        val review = reviewService.findById(reviewId).get()
        review.rate(valorationDTO.userId,valorationDTO.platform,valorationDTO.valoration)
        reviewService.update(review = review)

        return ReviewDTO.fromModel(review)

    }

    @GetMapping("/search")
    fun getReviewsFrom(@RequestParam contentId:String):List<ReviewDTO>{
        return reviewService.getReviewsWithContentId(contentId).map {
            ReviewDTO.fromModel(it)
        }
    }

    @GetMapping
    fun reviews(): List<ReviewDTO> {
        return reviewService.findAll().map { review -> ReviewDTO.fromModel(review) }
    }
}

data class CreateReviewRequest(val titleId: String, val reviewToCreate: ReviewDTO)
data class ValorarReview(val reviewId: Long, val valorationDTO: ValorationDTO)