package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.PerformedContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.*
import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.CreateReviewRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id


@Service
class ReviewService {

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Autowired
    private lateinit var performedContentRepository: PerformedContentRepository

    //used to setup configuration on Redis
    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var topic: ChannelTopic

    @Transactional
    fun saveReview(createReviewRequest: CreateReviewRequest): ReviewDTO {
        val content = contentRepository.findById(createReviewRequest.titleId).get()
        val review = createReviewRequest.reviewToCreate.toModel()
        review.cinematographicContent = content
        review.validate()

        content.addRate(review)

        performedContentRepository.save(PerformedContent.from(content))
        redisTemplate.convertAndSend(topic.topic, createReviewRequest.titleId)
        println("Event published")
        return saveReview(review)
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
    @Cacheable(key = "#titleId", value = ["fast-content"])
    fun performedSearchFor(titleId: String): PerformedContent {
        println("Le pegue a la bdd")
        return performedContentRepository.findById(titleId).get()
    }

    @Transactional
    fun findContentBy(reverseSearchFilter: ReverseSearchFilter): List<CinematographicContentDTO> {
        return reviewRepository.findContentInReverseSearch(reverseSearchFilter)
            .map { CinematographicContentDTO.fromModel(it) }
    }

    @Transactional
    fun findAll(): List<ReviewDTO> = reviewRepository.findAll().map { ReviewDTO.fromModel(it) }


    private fun saveReview(review: Review) = ReviewDTO.fromModel(reviewRepository.save(review))
}

@Entity
class PerformedContent(
    @Id
    val titleId: String,
    val averageRating: Double,
    val votesAmount: Int
) : Serializable {

    companion object {
        fun from(content: CinematographicContent): PerformedContent {
            return PerformedContent(content.titleId, content.averageRating, content.votesAmount)
        }
    }
}