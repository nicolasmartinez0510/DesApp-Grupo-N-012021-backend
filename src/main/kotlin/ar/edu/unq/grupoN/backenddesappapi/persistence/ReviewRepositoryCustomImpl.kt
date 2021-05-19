package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CastMember
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ApplicableFilters
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReverseSearchFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.*


@Repository
@Configuration
class ReviewRepositoryCustomImpl : ReviewRepositoryCustom {

    @Autowired
    private lateinit var em: EntityManager

    override fun findReviews(content: CinematographicContent, applicableFilters: ApplicableFilters): List<Review> {
        val cb = em.criteriaBuilder
        val cq: CriteriaQuery<Review> = cb.createQuery(Review::class.java)
        val review: Root<Review> = cq.from(Review::class.java)

        generateReviewFilters(content, applicableFilters, cb, review, cq)
        generateOrders(applicableFilters, cb, review, cq)

        val query: TypedQuery<Review> = em.createQuery(cq)
        val pageSize = 5
        query.firstResult = pageSize * applicableFilters.page
        query.maxResults = pageSize

        return query.resultList

    }

    override fun findContentInReverseSearch(reverseSearchFilter: ReverseSearchFilter): List<CinematographicContent> {
        val cb = em.criteriaBuilder
        val cq: CriteriaQuery<CinematographicContent> = cb.createQuery(CinematographicContent::class.java)
        val review: Root<Review> = cq.from(Review::class.java)
        val reviewAndContent: Join<Review, CinematographicContent> = review.join("cinematographicContent")
        val joinReviewContentCast: Join<Join<Review, CinematographicContent>, CastMember> =
            reviewAndContent.join("cast")

        val predicates: MutableList<Predicate> = ArrayList()

        if (reverseSearchFilter.reviewRating != null) {
            val predicate = cb.greaterThanOrEqualTo(review.get("rating"), reverseSearchFilter.reviewRating)
            predicates.add(predicate)
        }
        addEqual(predicates, cb, cb.upper(reviewAndContent.get("titleType")), reverseSearchFilter.genre?.toUpperCase())
        filterWellValued(predicates, cb, reverseSearchFilter.wellValued, review)
        filterDecade(predicates, cb, reverseSearchFilter, reviewAndContent)
        filterIsAdultContent(predicates, cb, reverseSearchFilter.isAdultContent, reviewAndContent)
        filterCastMember(predicates, cb, reverseSearchFilter, joinReviewContentCast)

        cq.where(*predicates.toTypedArray())

        cq.select(review.get("cinematographicContent"))
            .distinct(true)

        val query: TypedQuery<CinematographicContent> = em.createQuery(cq)

        return query.resultList

    }

    private fun generateReviewFilters(
        content: CinematographicContent,
        applicableFilters: ApplicableFilters,
        cb: CriteriaBuilder,
        review: Root<Review>,
        cq: CriteriaQuery<Review>
    ) {
        val predicates: MutableList<Predicate> = ArrayList()


        filterReviewType(applicableFilters, content, predicates, cb, review)

        addEqual(
            predicates,
            cb,
            review.get<CinematographicContent>("cinematographicContent").get("titleId"),
            content.titleId
        )
        addEqual(predicates, cb, review.get("platform"), applicableFilters.platform)

        filterTypeAndSpoiler(applicableFilters, predicates, cb, review)

        addEqual(predicates, cb, review.get("language"), applicableFilters.language)
        addEqual(predicates, cb, review.get("geographicLocation"), applicableFilters.geographicLocation)

        predicates.add(cb.lessThan(cb.size(review.get<MutableList<Report>>("reports")), 5))

        cq.where(*predicates.toTypedArray())
    }


    private fun <X> addEqual(
        predicateList: MutableList<Predicate>,
        cb: CriteriaBuilder,
        expression: Expression<X>,
        value: X?
    ) {
        if (value != null) predicateList.add(cb.equal(expression, value))
    }

    private fun generateOrders(
        applicableFilters: ApplicableFilters,
        cb: CriteriaBuilder,
        review: Root<Review>,
        cq: CriteriaQuery<Review>
    ) {
        val orders: MutableList<Order> = ArrayList()

        if (applicableFilters.orderByDate) {
            if (applicableFilters.order == "DESC") {
                orders.add(cb.desc(review.get<LocalDateTime>("date")))
            } else {
                orders.add(cb.asc(review.get<LocalDateTime>("date")))
            }
        }

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

    private fun filterTypeAndSpoiler(
        applicableFilters: ApplicableFilters,
        predicates: MutableList<Predicate>,
        cb: CriteriaBuilder,
        review: Root<Review>
    ) {
        if (applicableFilters.type != null) {
            if (applicableFilters.type == "PUBLIC") {
                addEqual(predicates, cb, review.get("isPublic"), true)

                if (applicableFilters.includeSpoiler != null) {
                    addEqual(predicates, cb, review.get("includeSpoiler"), applicableFilters.includeSpoiler)
                }
            } else {
                addEqual(predicates, cb, review.get("isPublic"), false)
            }
        }
    }

    private fun filterReviewType(
        applicableFilters: ApplicableFilters,
        content: CinematographicContent,
        predicates: MutableList<Predicate>,
        cb: CriteriaBuilder,
        review: Root<Review>
    ) {
        if (applicableFilters.contentType != null) {
            validateContentType(content, applicableFilters.contentType)

            addEqual(predicates, cb, review.get("reviewType"), applicableFilters.contentType)

            if (applicableFilters.contentType == ReviewType.CHAPTER) {
                if (applicableFilters.seasonNumber != null && applicableFilters.episodeNumber != null) {
                    addEqual(predicates, cb, review.get("seasonNumber"), applicableFilters.seasonNumber)
                    addEqual(predicates, cb, review.get("episodeNumber"), applicableFilters.episodeNumber)
                } else {
                    throw InvalidSeasonOrEpisodeNumberException()
                }
            }
        }
    }

    private fun filterWellValued(
        predicates: MutableList<Predicate>,
        cb: CriteriaBuilder,
        wellValued: Boolean?,
        review: Root<Review>
    ) {
        if (wellValued != null) {
            if (wellValued) {
                val predicate = cb.greaterThanOrEqualTo(review.get("valoration"), 5)
                predicates.add(predicate)
            } else {
                val predicate = cb.lessThan(review.get("valoration"), 0)
                predicates.add(predicate)
            }
        }
    }


    fun filterDecade(
        predicates: MutableList<Predicate>,
        cb: CriteriaBuilder,
        reverseSearchFilter: ReverseSearchFilter,
        reviewAndContent: Join<Review, CinematographicContent>
    ) {
        if (reverseSearchFilter.decade != null) {
            if (reverseSearchFilter.decade < 1900) throw RuntimeException("Invalid year decade. Insert a value equals or greather than 1900.")

            val predicateStartDecade =
                cb.greaterThanOrEqualTo(reviewAndContent.get("startYear"), reverseSearchFilter.decade)
            val predicateEndDecade =
                cb.lessThanOrEqualTo(reviewAndContent.get("startYear"), reverseSearchFilter.decade + 10)

            predicates.add(predicateStartDecade)
            predicates.add(predicateEndDecade)
        }

    }

    private fun filterCastMember(
        predicates: MutableList<Predicate>,
        cb: CriteriaBuilder,
        reverseSearchFilter: ReverseSearchFilter,
        joinReviewContentCast: Join<Join<Review, CinematographicContent>, CastMember>
    ) {
        if (reverseSearchFilter.searchedCastMember != null) {
            if (reverseSearchFilter.jobInContent != null) {
                if (reverseSearchFilter.jobInContent == Employment.ACTOR) {
                    addEqual(predicates, cb, joinReviewContentCast.get("employment"), Employment.ACTOR)
//                    val castType = cb.equal(joinReviewContentCast.get<Employment>("employment"), Employment.ACTOR)
//                    predicates.add(castType)
                }
                if (reverseSearchFilter.jobInContent == Employment.DIRECTOR) {
                    addEqual(predicates, cb, joinReviewContentCast.get("employment"), Employment.DIRECTOR)
//                    val castType = cb.equal(joinReviewContentCast.get<Employment>("employment"), Employment.DIRECTOR)
//                    predicates.add(castType)
                }
                if (reverseSearchFilter.jobInContent == Employment.WRITER) {
                    addEqual(predicates, cb, joinReviewContentCast.get("employment"), Employment.WRITER)
//                    val castType = cb.equal(joinReviewContentCast.get<Employment>("employment"), Employment.WRITER)
//                    predicates.add(castType)
                }
            }

            val actorPredicate = cb.like(
                cb.upper(joinReviewContentCast.get("name")),
                "%" + reverseSearchFilter.searchedCastMember.toUpperCase() + "%"
            )
            predicates.add(actorPredicate)
        }
    }

    private fun filterIsAdultContent(
        predicates: MutableList<Predicate>,
        cb: CriteriaBuilder,
        adultContent: Boolean?,
        reviewAndContent: Join<Review, CinematographicContent>
    ) {
        if (adultContent != null) {
            if (adultContent) {
                addEqual(predicates, cb, reviewAndContent.get("isAdultContent"), true)
            } else {
                addEqual(predicates, cb, reviewAndContent.get("isAdultContent"), false)
            }
        }
    }

    private fun validateContentType(content: CinematographicContent, contentType: ReviewType) {
        if (!content.isSerie() && (contentType == ReviewType.SERIE || contentType == ReviewType.CHAPTER))
            throw  InvalidReviewTypeException("Invalid review type, '${content.title}' is a Movie")

        if (content.isSerie() && contentType == ReviewType.MOVIE)
            throw  InvalidReviewTypeException("Invalid review type, '${content.title}' is a Serie")
    }

}