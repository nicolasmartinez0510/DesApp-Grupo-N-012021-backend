package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class ReviewService {

    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Transactional
    fun saveReview(titleId: String, reviewDTO: ReviewDTO): ReviewDTO {
        val content = contentRepository.findById(titleId).get()
        val review = reviewDTO.toModel()
        review.cinematographicContent = content
        review.validate()

        return saveAndNotify(review, titleId)
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
            .map {
                val model = CinematographicContentDTO.fromModel(it)
                if (model.isMovie()) {
                    val movie = model as MovieDTO
                    val info = reviewRepository.contentBasicInfo(movie.titleId)
                    movie.rating = info.averageRating
                    movie.votesAmount = info.votesAmount
                    movie
                } else {
                    val serie = model as SerieDTO
                    val info = reviewRepository.contentBasicInfo(serie.titleId)
                    serie.rating = info.averageRating
                    serie.votesAmount = info.votesAmount
                    serie
                }
            }
    }

    @Transactional
    fun contentsInfoAccessedAfter(lastWork: LocalDateTime?): MutableList<PerformedContent> {
        return reviewRepository.contentsInfoAccessedAfter(lastWork)
    }

    private fun saveAndNotify(
        review: Review,
        titleId: String
    ): ReviewDTO {
        redisTemplate.convertAndSend("pubsub:review-channel", titleId)
        return saveReview(review)
    }

    private fun saveReview(review: Review) = ReviewDTO.fromModel(reviewRepository.save(review))
}