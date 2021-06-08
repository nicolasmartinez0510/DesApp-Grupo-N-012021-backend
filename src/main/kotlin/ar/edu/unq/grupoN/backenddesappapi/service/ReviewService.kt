package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepositoryCustomImpl
import ar.edu.unq.grupoN.backenddesappapi.service.dto.*
import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.CreateReviewRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ReviewService {

    @Autowired
    private lateinit var basicReviewRepository: ReviewRepository

    @Autowired
    private lateinit var customReviewRepository: ReviewRepositoryCustomImpl

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Transactional
    fun saveReview(createReviewRequest: CreateReviewRequest): ReviewDTO {
        val content = contentRepository.findById(createReviewRequest.titleId).get()
        val review = createReviewRequest.reviewToCreate.toModel()
        review.cinematographicContent = content

        review.validate()

        return saveReview(review)
    }

    @Transactional
    fun rate(reviewId: Long, valorationDTO: ValorationDTO): ReviewDTO {
        val review: Review = basicReviewRepository.findById(reviewId).get()

        review.rate(valorationDTO.userId, valorationDTO.platform, valorationDTO.valoration)

        return saveReview(review)
    }

    @Transactional
    fun report(reviewId: Long, reportDTO: ReportDTO): ReviewDTO {
        val review: Review = basicReviewRepository.findById(reviewId).get()
        if(!review.isPublic) throw RuntimeException("Cannot report a premium review.")
        review.report(reportDTO.userId, reportDTO.platform, reportDTO.reportType)

        return saveReview(review)
    }

    @Transactional
    fun findContentBy(reverseSearchFilter: ReverseSearchFilter): List<CinematographicContentDTO> {
        return customReviewRepository.findContentInReverseSearch(reverseSearchFilter)
            .map { CinematographicContentDTO.fromModel(it) }
    }

    @Transactional
    fun search(titleId: String, applicableFilters: ApplicableFilters): List<ReviewDTO> {
        val content = contentRepository.findById(titleId).get()

        val reviews = customReviewRepository.findReviews(content, applicableFilters)

        return reviews.map { ReviewDTO.fromModel(it) }
    }

    @Transactional
    fun addFakeReview(review: Review) {
        basicReviewRepository.save(review)
    }

    @Transactional
    fun findAll(): List<ReviewDTO> = basicReviewRepository.findAll().map { ReviewDTO.fromModel(it) }


    private fun saveReview(review: Review) = ReviewDTO.fromModel(basicReviewRepository.save(review))
}