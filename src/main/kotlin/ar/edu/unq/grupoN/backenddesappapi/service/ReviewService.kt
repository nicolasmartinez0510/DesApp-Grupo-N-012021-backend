package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ValorationDTO
import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.CreateReviewRequest
import io.swagger.annotations.ApiModelProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Order
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


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

    //    Buscar reseñas de una película o serie usando el id de IMDB. Se debe poder filtrar por
//    plataforma, spoiler alert, tipo (review o crítica), idioma y país. Además que se pueda
//    ordenar por rating y/o fecha, y que el orden sea ascendente o descendente. Además,
//    los resultados deben estar paginados
    @Transactional
    fun search(titleId: String, applicableFilters: ApplicableFilters): List<ReviewDTO> {
        val cb = em.criteriaBuilder
        val cq: CriteriaQuery<Review> = cb.createQuery(Review::class.java)
        val predicates: MutableList<Predicate> = ArrayList()

        val review: Root<Review> = cq.from(Review::class.java)
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

        cq.where(*predicates.toTypedArray())


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

        cq.orderBy(*orders.toTypedArray())

        val query: TypedQuery<Review> = em.createQuery(cq)

        val pageSize = 5
        query.firstResult = pageSize * applicableFilters.page
        query.maxResults = pageSize

        return query.resultList.map { ReviewDTO.fromModel(it) }
    }

    @Transactional
    fun addFakeReview(review: Review) {
        repository.save(review)
    }

    @Transactional
    fun findAll(): List<ReviewDTO> = repository.findAll().map { ReviewDTO.fromModel(it) }

    private fun validateContentType(content: CinematographicContent, contentType: ReviewType) {
        if (!content.isSerie() && (contentType == ReviewType.SERIE || contentType == ReviewType.CHAPTER))
            throw  InvalidReviewTypeException("Invalid review type, '${content.title}' is a Movie")

        if (content.isSerie() && contentType == ReviewType.MOVIE)
            throw  InvalidReviewTypeException("Invalid review type, '${content.title}' is a Serie")
    }
}

//    Buscar reseñas de una película o serie usando el id de IMDB. Se debe poder filtrar por
//    plataforma, spoiler alert, tipo (review o crítica), idioma y país. Además que se pueda
//    ordenar por rating y/o fecha, y que el orden sea ascendente o descendente. Además,
//    los resultados deben estar paginados
data class ApplicableFilters(
    val platform: String? = null,
    val includeSpoiler: Boolean? = null,
    val type: String? = null,
    val language: String? = null,
    val geographicLocation: String? = null,
    val contentType: ReviewType? = null,
    val seasonNumber: Int? = null,
    val episodeNumber: Int? = null,
    val orderByDate: Boolean = true,
    val orderByRating: Boolean = true,
    val order: String? = "DESC",
    val page: Int = 0
)
