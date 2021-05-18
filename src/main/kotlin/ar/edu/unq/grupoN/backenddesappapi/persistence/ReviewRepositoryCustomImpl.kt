package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.InvalidReviewTypeException
import ar.edu.unq.grupoN.backenddesappapi.model.InvalidSeasonOrEpisodeNumberException
import ar.edu.unq.grupoN.backenddesappapi.model.Report
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewType
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CastMember
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ApplicableFilters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.*


@Repository
@Configuration
class ReviewRepositoryCustomImpl: ReviewRepositoryCustom {

    @Autowired
    private lateinit var em: EntityManager

    override fun findReviews(content:CinematographicContent, applicableFilters: ApplicableFilters) : List<Review> {
        val cb = em.criteriaBuilder
        val cq: CriteriaQuery<Review> = cb.createQuery(Review::class.java)
        val review: Root<Review> = cq.from(Review::class.java)

        generateFilters(content, applicableFilters, cb, review, cq)
        generateOrders(applicableFilters, cb, review, cq)

        val query: TypedQuery<Review> = generateQuery(cq, applicableFilters)

        return query.resultList

    }

    override fun findContentInReverseSearch(): List<CinematographicContent> {
        val cb = em.criteriaBuilder
        val cq: CriteriaQuery<CinematographicContent> = cb.createQuery(CinematographicContent::class.java)
        val review: Root<Review> = cq.from(Review::class.java)
        val join: Join<Review, CinematographicContent> = review.join("cinematographicContent")
        val joinb: Join<Join<Review,CinematographicContent>, CastMember> = join.join("cast")

        cq.where(
            cb.like(cb.upper(joinb.get("name")),"%rsAurio%".toUpperCase())
        )


        cq.select(review.get("cinematographicContent"))
            .distinct(true)

        val query: TypedQuery<CinematographicContent> = em.createQuery(cq)

        return query.resultList

    }


    private fun generateFilters(
        content: CinematographicContent,
        applicableFilters: ApplicableFilters,
        cb: CriteriaBuilder,
        review: Root<Review>,
        cq: CriteriaQuery<Review>
    ) {
        val predicates: MutableList<Predicate> = ArrayList()


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
            cb.equal(review.get<CinematographicContent>("cinematographicContent").get<String>("titleId"), content.titleId)
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

}