package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.ValorationData
import ar.edu.unq.grupoN.backenddesappapi.model.review.*
import java.time.LocalDateTime

abstract class ReviewDTO {

    companion object {
        fun fromModel(review: Review): ReviewDTO {
            return if (review.isPublic()) {
                val newReview = review as Public
                PublicDTO(review.cinematographicContent.titleId,
                review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType,
                    review.valorations.map { ValorationDTO(it) }, newReview.includeSpoiler, newReview.userId,
                    newReview.username, newReview.geographicLocation)
            } else {
                val newReview = review as Premium
                PremiumDTO(review.cinematographicContent.titleId,
                    review.platform, review.language, review.resumeText, review.text, review.rating, review.date,
                    review.seasonNumber, review.episodeNumber, review.reviewType,
                    review.valorations.map { ValorationDTO(it) }, newReview.reviewerId)
            }
        }
    }

    open fun isPublic() = false
}

class PublicDTO(val cinematographicContentTitleId: String,
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

    override fun isPublic() = true
}

class PremiumDTO(val cinematographicContentTitleId: String,
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
                 val reviewerId: String) : ReviewDTO()


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
