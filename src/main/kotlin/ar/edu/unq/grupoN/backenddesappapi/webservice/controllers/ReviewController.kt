package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.model.ContentInfo
import ar.edu.unq.grupoN.backenddesappapi.model.PublicReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Premium
import ar.edu.unq.grupoN.backenddesappapi.model.review.Public
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.CinematographicContentService
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PremiumDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PublicDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import org.springframework.web.bind.annotation.*

@ServiceREST
class ReviewController(reviewRepository : ReviewRepository, contentRepository: CinematographicContentRepository) {
    private var reviewService: ReviewService = ReviewService(reviewRepository)
    private var contentService: CinematographicContentService = CinematographicContentService(contentRepository)

    @PostMapping("/review/add")
    fun addReview(@RequestBody reviewDTO: PublicDTO) {
        val newReview = toModel(reviewDTO)

        reviewService.saveReview(newReview)
    }


    private fun toModel(reviewDTO: ReviewDTO): Review {
        val myReviewDTO = reviewDTO as PublicDTO
        val content = contentService.findById(myReviewDTO.cinematographicContentTitleId).get()
        val contentInfo = ContentInfo(content, myReviewDTO.platform, myReviewDTO.seasonNumber, myReviewDTO.episodeNumber)
        val reviewInfo = ReviewInfo(myReviewDTO.resumeText, myReviewDTO.text, myReviewDTO.rating, myReviewDTO.date, myReviewDTO.reviewType, myReviewDTO.language)

        return if (myReviewDTO.isPublic()){
            val publicReviewInfo = PublicReviewInfo(myReviewDTO.includeSpoiler,myReviewDTO.username, myReviewDTO.userId, myReviewDTO.geographicLocation)

            Public(contentInfo, reviewInfo, publicReviewInfo)
        } else {
            val premiumReviewDTO = myReviewDTO as PremiumDTO
            Premium(contentInfo, reviewInfo, premiumReviewDTO.reviewerId)
        }
    }
}