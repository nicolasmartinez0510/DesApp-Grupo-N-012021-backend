package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.PerformedContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ReviewService {

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var performedContentRepository: PerformedContentRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var topic: ChannelTopic

    @Transactional
    fun saveReview(titleId: String, reviewDTO: ReviewDTO): ReviewDTO {
        val content = contentRepository.findById(titleId).get()
        val review = reviewDTO.toModel()
        review.cinematographicContent = content
        review.validate()

        content.addRate(review)

        return saveAndNotify(review, content, titleId)
    }

    @Transactional
    fun rate(reviewId: Long, valorationDTO: ValorationDTO): ReviewDTO {
        val review: Review = reviewRepository.findById(reviewId).get()

        review.rate(valorationDTO.userId, valorationDTO.platform, valorationDTO.valoration)

        return saveReview(review)
    }

    @Transactional
    fun report(reviewId: Long, reportDTO: ReportDTO): ReviewDTO {
        val review: Review = reviewRepository.findById(reviewId).get()
        if (!review.isPublic) throw RuntimeException("Cannot report a premium review.")
        review.report(reportDTO.userId, reportDTO.platform, reportDTO.reportType)

        return saveReview(review)
    }

    @Transactional
    fun search(titleId: String, applicableFilters: ApplicableFilters): List<ReviewDTO> {
        val content = contentRepository.findById(titleId).get()

        val reviews = reviewRepository.findReviews(content, applicableFilters)

        return reviews.map { ReviewDTO.fromModel(it) }
    }

    @Transactional
    fun findContentBy(reverseSearchFilter: ReverseSearchFilter): List<CinematographicContentDTO> {
        return reviewRepository.findContentInReverseSearch(reverseSearchFilter)
            .map { CinematographicContentDTO.fromModel(it) }
    }

    private fun saveAndNotify(
        review: Review,
        content: CinematographicContent,
        titleId: String
    ): ReviewDTO {
        performedContentRepository.save(PerformedContent.from(content))
        redisTemplate.convertAndSend(topic.topic, titleId)
        return saveReview(review)
    }

    private fun saveReview(review: Review) = ReviewDTO.fromModel(reviewRepository.save(review))
}