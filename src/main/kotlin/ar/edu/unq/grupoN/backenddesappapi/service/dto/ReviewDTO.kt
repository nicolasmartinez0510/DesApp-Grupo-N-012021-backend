package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.ValorationData
import ar.edu.unq.grupoN.backenddesappapi.model.review.*
import java.time.LocalDateTime

abstract class ReviewDTO(review: Review) {
    var cinematographicContentTitleId: String
    var platform: String
    var language: String
    var resumeText: String
    var text: String
    var rating: Rating
    var date: LocalDateTime
    var seasonNumber: Int? = null
    var episodeNumber: Int? = null
    var valorations: List<ValorationDTO>

    companion object {
        fun fromModel(review: Review): ReviewDTO {
            return if (review.isPublic()) {
                PublicDTO(review as Public)
            } else {
                PremiumDTO(review as Premium)
            }
        }
    }

    init {
        this.cinematographicContentTitleId = review.cinematographicContent.titleId
        this.platform = review.platform
        this.language = review.language
        this.resumeText = review.resumeText
        this.text = review.text
        this.rating = review.rating
        this.date = review.date
        this.valorations = review.valorations.map { ValorationDTO(it) }

        checkIfChapterReview(review)
    }

    fun checkIfChapterReview(review: Review) {
        if (review.reviewType == ReviewType.CHAPTER) {
            this.seasonNumber = review.seasonNumber
            this.episodeNumber = review.episodeNumber
        }
    }
}

class PublicDTO(public_review: Public) : ReviewDTO(public_review) {
    var includeSpoiler: Boolean = false
    var userId: String
    var username: String
    var geographicLocation: String

    init {
        this.includeSpoiler = public_review.includeSpoiler
        this.userId = public_review.userId
        this.username = public_review.username
        this.geographicLocation = public_review.geographicLocation
    }
}

class PremiumDTO(premium_review: Premium) : ReviewDTO(premium_review) {
    var reviewerId: String

    init {
        this.reviewerId = premium_review.reviewerId
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
