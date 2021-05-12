package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.InvalidReviewTypeException
import ar.edu.unq.grupoN.backenddesappapi.model.InvalidSeasonOrEpisodeNumberException
import ar.edu.unq.grupoN.backenddesappapi.model.Report
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewType
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ApplicableFilters
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReportDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ValorationDTO
import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.CreateReviewRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.*


@Service
class ReviewService {

    @Autowired
    private lateinit var repository: ReviewRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Autowired
    private lateinit var em: EntityManager

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
        val review: Review = repository.findById(reviewId).get()

        review.rate(valorationDTO.userId, valorationDTO.platform, valorationDTO.valoration)

        return saveReview(review)
    }

    @Transactional
    fun report(reviewId: Long, reportDTO: ReportDTO): ReviewDTO {
        val review: Review = repository.findById(reviewId).get()

        review.report(reportDTO.userId, reportDTO.platform, reportDTO.reportType)

        return saveReview(review)
    }

    @Transactional
    fun search(titleId: String, applicableFilters: ApplicableFilters): List<ReviewDTO> {
        val cb = em.criteriaBuilder
        val cq: CriteriaQuery<Review> = cb.createQuery(Review::class.java)
        val review: Root<Review> = cq.from(Review::class.java)

        generateFilters(titleId, applicableFilters, cb, review, cq)
        generateOrders(applicableFilters, cb, review, cq)

        val query: TypedQuery<Review> = generateQuery(cq, applicableFilters)

        return query.resultList.map { ReviewDTO.fromModel(it) }
    }

    @Transactional
    fun addFakeReview(review: Review) {
        repository.save(review)
    }

    @Transactional
    fun findAll(): List<ReviewDTO> = repository.findAll().map { ReviewDTO.fromModel(it) }

    private fun generateFilters(
        titleId: String,
        applicableFilters: ApplicableFilters,
        cb: CriteriaBuilder,
        review: Root<Review>,
        cq: CriteriaQuery<Review>
    ) {
        val predicates: MutableList<Predicate> = ArrayList()

        val content = contentRepository.findById(titleId).get()

        //filtering review's content type
        if (applicableFilters.contentType != null) {
            validateContentType(content, applicableFilters.contentType)

            val contentTypePredicate = cb.equal(review.get<String>("reviewType"), applicableFilters.contentType)
            predicates.add(contentTypePredicate)

            if (applicableFilters.contentType == ReviewType.CHAPTER) {
                if (applicableFilters.seasonNumber != null && applicableFilters.episodeNumber != null) {
                    val seasonPredicate = cb.equal(review.get<Int>("seasonNumber"), applicableFilters.seasonNumber)
                    val episodePredicate = cb.equal(review.get<Int>("episodeNumber"), applicableFilters.episodeNumber)
                    predicates.add(seasonPredicate)
                    predicates.add(episodePredicate)
                } else {
                    throw InvalidSeasonOrEpisodeNumberException()
                }
            }
        }

        //filtering title id
        val titleIdPredicate: Predicate =
            cb.equal(review.get<CinematographicContent>("cinematographicContent").get<String>("titleId"), titleId)
        predicates.add(titleIdPredicate)


        //filtering platform
        if (applicableFilters.platform != null) {
            val platform = cb.equal(review.get<String>("platform"), applicableFilters.platform)
            predicates.add(platform)
        }

        //filtering review type and spoilerAlert
        if (applicableFilters.type != null) {
            if (applicableFilters.type == "PUBLIC") {
                val isPublicReview: Predicate = cb.equal(review.get<Boolean>("isPublic"), true)
                predicates.add(isPublicReview)

                if (applicableFilters.includeSpoiler != null) {
                    val withSpoiler: Predicate =
                        cb.equal(review.get<Boolean>("includeSpoiler"), applicableFilters.includeSpoiler)
                    predicates.add(withSpoiler)
                }
            } else {
                val isPublic = cb.equal(review.get<String>("isPublic"), false)
                predicates.add(isPublic)
            }
        }

        //filtering language
        if (applicableFilters.language != null) {
            val languagePredicate: Predicate = cb.equal(review.get<Boolean>("language"), applicableFilters.language)
            predicates.add(languagePredicate)
        }

        //filtering location
        if (applicableFilters.geographicLocation != null) {
            val geographicLocationPredicate: Predicate =
                cb.equal(review.get<Boolean>("geographicLocation"), applicableFilters.geographicLocation)
            predicates.add(geographicLocationPredicate)
        }

        predicates.add(cb.lessThan(cb.size(review.get<MutableList<Report>>("reports")), 5))

        cq.where(*predicates.toTypedArray())
    }

    private fun generateOrders(
        applicableFilters: ApplicableFilters,
        cb: CriteriaBuilder,
        review: Root<Review>,
        cq: CriteriaQuery<Review>
    ) {
        val orders: MutableList<Order> = ArrayList()


        //order by date
        if (applicableFilters.orderByDate) {
            if (applicableFilters.order == "DESC") {
                orders.add(cb.desc(review.get<LocalDateTime>("date")))
            } else {
                orders.add(cb.asc(review.get<LocalDateTime>("date")))
            }
        }

        //order by valoration
        if (applicableFilters.orderByRating) {
            if (applicableFilters.order == "DESC") {
                orders.add(cb.desc(review.get<Int>("valoration")))
            } else {
                orders.add(cb.asc(review.get<Int>("valoration")))
            }
        }

        orders.add(cb.asc(cb.size(review.get<MutableList<Report>>("reports"))))

        cq.orderBy(*orders.toTypedArray())
    }

    private fun generateQuery(
        cq: CriteriaQuery<Review>,
        applicableFilters: ApplicableFilters
    ): TypedQuery<Review> {
        val query: TypedQuery<Review> = em.createQuery(cq)
        val pageSize = 5
        query.firstResult = pageSize * applicableFilters.page
        query.maxResults = pageSize
        return query
    }

    private fun validateContentType(content: CinematographicContent, contentType: ReviewType) {
        if (!content.isSerie() && (contentType == ReviewType.SERIE || contentType == ReviewType.CHAPTER))
            throw  InvalidReviewTypeException("Invalid review type, '${content.title}' is a Movie")

        if (content.isSerie() && contentType == ReviewType.MOVIE)
            throw  InvalidReviewTypeException("Invalid review type, '${content.title}' is a Serie")
    }

    private fun saveReview(review: Review) = ReviewDTO.fromModel(repository.save(review))
}