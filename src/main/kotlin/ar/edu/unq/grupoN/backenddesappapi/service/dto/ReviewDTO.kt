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
                val publicReview = review as Public
                PublicDTO(review.id,review.cinematographicContent!!.titleId,
                review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType,
                    review.valorations.map { ValorationDTO.fromModel(it) }, publicReview.includeSpoiler, publicReview.userId,
                    publicReview.username, publicReview.geographicLocation)
            } else {
                val premiumReview = review as Premium
                PremiumDTO(review.id,review.cinematographicContent!!.titleId,
                    review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType,
                    review.valorations.map { ValorationDTO.fromModel(it) }, premiumReview.reviewerId)
            }
        }
    }

    abstract fun toModel(): Review
}

class PublicDTO(
                val id: Long?,
                val cinematographicContentTitleId: String?,
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

class PremiumDTO(
                 val id: Long?,
                 val cinematographicContentTitleId: String?,
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


data class ValorationDTO(
    var userId: String,
    var platform: String,
    var valoration: Valoration)
{
    companion object {
        fun fromModel(valorationData: ValorationData): ValorationDTO {
            return ValorationDTO(
                valorationData.userId,
                valorationData.platform,
                valorationData.valoration
            )
        }
    }
}
