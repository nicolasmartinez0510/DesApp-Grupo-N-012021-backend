package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.review.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime
import javax.validation.constraints.*

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
            return if (review.isPublic) {
                val publicReview = review as Public
                PublicDTO(review.id,review.cinematographicContent!!.titleId,
                review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType, review.valoration ,
                    publicReview.reports.size, publicReview.includeSpoiler, publicReview.userId,
                    publicReview.username, publicReview.geographicLocation)
            } else {
                val premiumReview = review as Premium
                PremiumDTO(review.id,review.cinematographicContent!!.titleId,
                    review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType, review.valoration,
                    premiumReview.reports.size, premiumReview.geographicLocation, premiumReview.reviewerId)
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
    @field:NotBlank
    val platform: String,
    @field:NotBlank
    val language: String,
    @field:NotBlank
    val resumeText: String,
    @field:NotBlank
    val text: String,
    @field:Min(value = 0)
    @field:Max(value=5)
    val rating: Double,
    @field:NotNull
    @field:PastOrPresent
    val date: LocalDateTime,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    @field:Min(value = 1)
    val seasonNumber: Int? = null,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    @field:Min(value = 1)
    val episodeNumber: Int? = null,
    val reviewType: ReviewType,
    @ApiModelProperty(hidden = true)
    val valoration: Int = 0,
    @ApiModelProperty(hidden = true)
    val reportsAmount: Int = 0,
    @field:NotNull
    val includeSpoiler: Boolean = false,
    @field:NotBlank
    val userId: String,
    @field:NotBlank
    val username: String,
    @field:NotBlank
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
    @field:NotBlank
    val platform: String,
    @field:NotBlank
    val language: String,
    @field:NotBlank
    val resumeText: String,
    @field:NotBlank
    val text: String,
    @field:Min(value = 0)
    @field:Max(value=5)
    val rating: Double,
    @field:NotNull
    @field:PastOrPresent
    val date: LocalDateTime,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    @field:Min(value = 1)
    val seasonNumber: Int? = null,
    @ApiModelProperty(value = "This field is only used when you want to add a review on a specific chapter in a Serie.")
    @field:Min(value = 1)
    val episodeNumber: Int? = null,
    val reviewType: ReviewType,
    @ApiModelProperty(hidden = true)
    val valoration: Int = 0,
    @ApiModelProperty(hidden = true)
    val reportsAmount: Int = 0,
    @field:NotBlank
    val geographicLocation: String,
    @field:NotBlank
    val reviewerId: String, ) : ReviewDTO() {

    override fun toModel(): Review {
        val contentInfo = ContentInfo(null, platform, seasonNumber, episodeNumber)
        val reviewInfo = ReviewInfo(resumeText, text, rating, date, reviewType, language, geographicLocation)

        return Premium(contentInfo, reviewInfo, reviewerId)
    }
}

@ApiModel(description = "Represents information to valorate a review.")
data class ValorationDTO(
    @ApiModelProperty(value = "userId", example = "Chester", required = true)
    @field:NotBlank
    var userId: String,
    @ApiModelProperty(value = "platform", example = "Netflix", required = true)
    @field:NotBlank
    var platform: String,
    @ApiModelProperty(value = "valoration", example = "LIKE", required = true)
    var valoration: Valoration
)

@ApiModel(description = "Represents information to report a review.")
data class ReportDTO(

    @ApiModelProperty(value = "userId", example = "Chester", required = true)
    @field:NotBlank
    var userId: String,
    @ApiModelProperty(value = "platform", example = "Netflix", required = true)
    @field:NotBlank
    var platform: String,
    @ApiModelProperty(value = "report", example = "SPAM", required = true)
    var reportType: ReportType
)
