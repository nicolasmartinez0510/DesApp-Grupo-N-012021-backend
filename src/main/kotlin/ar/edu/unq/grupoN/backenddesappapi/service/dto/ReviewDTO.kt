package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.ContentInfo
import ar.edu.unq.grupoN.backenddesappapi.model.PublicReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ReviewInfo
import ar.edu.unq.grupoN.backenddesappapi.model.ValorationData
import ar.edu.unq.grupoN.backenddesappapi.model.review.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PublicDTO::class, name = "PUBLIC"),
    JsonSubTypes.Type(value = PremiumDTO::class, name = "PREMIUM")
)
abstract class ReviewDTO {

    companion object {
        fun fromModel(review: Review): ReviewDTO {
            return if (review.isPublic()) {
                val newReview = review as Public
                PublicDTO(review.cinematographicContent!!.titleId,
                review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType,
                    review.valorations.map { ValorationDTO(it) }, newReview.includeSpoiler, newReview.userId,
                    newReview.username, newReview.geographicLocation)
            } else {
                val newReview = review as Premium
                PremiumDTO(review.cinematographicContent!!.titleId,
                    review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType,
                    review.valorations.map { ValorationDTO(it) }, newReview.reviewerId)
            }
        }
    }

    abstract fun toModel(): Review
}

class PublicDTO(val cinematographicContentTitleId: String?,
                val platform: String,
                val language: String,
                val resumeText: String,
                val text: String,
                val rating: Rating,
                val date: LocalDateTime,
                val seasonNumber: Int? = null,
                val episodeNumber: Int? = null,
                val reviewType: ReviewType,
                val valorations: List<ValorationDTO> = mutableListOf(),
                val includeSpoiler: Boolean = false,
                val userId: String,
                val username: String,
                val geographicLocation: String) : ReviewDTO() {

    override fun toModel(): Review {
        val contentInfo = ContentInfo(null, platform, seasonNumber, episodeNumber)
        val reviewInfo = ReviewInfo(resumeText, text, rating, date, reviewType, language)
        val publicReviewInfo = PublicReviewInfo(includeSpoiler, username, userId, geographicLocation)

        return Public(contentInfo, reviewInfo, publicReviewInfo)
    }
}

class PremiumDTO(val cinematographicContentTitleId: String?,
                 val platform: String,
                 val language: String,
                 val resumeText: String,
                 val text: String,
                 val rating: Rating,
                 val date: LocalDateTime,
                 val seasonNumber: Int? = null,
                 val episodeNumber: Int? = null,
                 val reviewType: ReviewType,
                 val valorations: List<ValorationDTO> = mutableListOf(),
                 val reviewerId: String) : ReviewDTO() {

    override fun toModel(): Review {
        val contentInfo = ContentInfo(null, platform, seasonNumber, episodeNumber)
        val reviewInfo = ReviewInfo(resumeText, text, rating, date, reviewType, language)

        return Premium(contentInfo, reviewInfo, reviewerId)
    }
}


class ValorationDTO(valorationData: ValorationData) {
    var userId: String
    var platform: String
    var valoration: Valoration

    init {
        this.userId = valorationData.userId
        this.platform = valorationData.platform
        this.valoration = valorationData.valoration
    }

}
