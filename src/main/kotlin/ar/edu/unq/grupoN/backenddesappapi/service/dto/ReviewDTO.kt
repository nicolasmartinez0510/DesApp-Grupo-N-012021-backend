package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.review.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PublicDTO::class, name = "PUBLIC"),
    JsonSubTypes.Type(value = PremiumDTO::class, name = "PREMIUM")
)
@ApiModel(subTypes = [PublicDTO::class, PremiumDTO::class], discriminator = "@type",
    description = "Superclass of reviews.")
abstract class ReviewDTO {

    companion object {
        fun fromModel(review: Review): ReviewDTO {
            return if (review.isPublic()) {
                val publicReview = review as Public
                PublicDTO(review.id,review.cinematographicContent!!.titleId,
                review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType, review.valoration,
                    publicReview.includeSpoiler, publicReview.userId,
                    publicReview.username, publicReview.geographicLocation)
            } else {
                val premiumReview = review as Premium
                PremiumDTO(review.id,review.cinematographicContent!!.titleId,
                    review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType, review.valoration,
                    premiumReview.geographicLocation, premiumReview.reviewerId)
            }
        }
    }

    abstract fun toModel(): Review
}

@ApiModel(description = "Represents all information of a public review.", parent = ReviewDTO::class)
class PublicDTO(
    @ApiModelProperty(hidden = true)
    val id: Long?,
    @ApiModelProperty(hidden = true)
    val cinematographicContentTitleId: String?,
    val platform: String,
    val language: String,
    val resumeText: String,
    val text: String,
    val rating: Rating,
    val date: LocalDateTime,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    val seasonNumber: Int? = null,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    val episodeNumber: Int? = null,
    val reviewType: ReviewType,
    @ApiModelProperty(hidden = true)
    val valoration: Int = 0,
    val includeSpoiler: Boolean = false,
    val userId: String,
    val username: String,
    val geographicLocation: String) : ReviewDTO() {

    override fun toModel(): Review {
        val contentInfo = ContentInfo(null, platform, seasonNumber, episodeNumber)
        val reviewInfo = ReviewInfo(resumeText, text, rating, date, reviewType, language, geographicLocation)
        val publicReviewInfo = PublicReviewInfo(includeSpoiler, username, userId)

        return Public(contentInfo, reviewInfo, publicReviewInfo)
    }
}

@ApiModel(description = "Represents all information of a premium review.", parent = ReviewDTO::class)
class PremiumDTO(
    @ApiModelProperty(hidden = true)
    val id: Long?,
    @ApiModelProperty(hidden = true)
    val cinematographicContentTitleId: String?,
    val platform: String,
    val language: String,
    val resumeText: String,
    val text: String,
    val rating: Rating,
    val date: LocalDateTime,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    val seasonNumber: Int? = null,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    val episodeNumber: Int? = null,
    val reviewType: ReviewType,
    @ApiModelProperty(hidden = true)
    val valoration: Int = 0,
    val geographicLocation: String,
    val reviewerId: String) : ReviewDTO() {

    override fun toModel(): Review {
        val contentInfo = ContentInfo(null, platform, seasonNumber, episodeNumber)
        val reviewInfo = ReviewInfo(resumeText, text, rating, date, reviewType, language, geographicLocation)

        return Premium(contentInfo, reviewInfo, reviewerId)
    }
}

@ApiModel(description = "Represents information to valorate a review.")
data class ValorationDTO(

    @ApiModelProperty(value = "userId", example = "Chester", required = true)
    var userId: String,
    @ApiModelProperty(value = "platform", example = "Netflix", required = true)
    var platform: String,
    @ApiModelProperty(value = "valoration", example = "LIKE", required = true)
    var valoration: Valoration
)
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
