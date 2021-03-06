package ar.edu.unq.grupoN.backenddesappapi.model

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.Season
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import java.time.LocalDateTime
import javax.persistence.*

data class BasicInformation(
    var titleId: String, val title: String, val titleType: String,
    val isAdultContent: Boolean, val startYear: Int, val runtimeMinutes: Int
)

data class RatingInfo(val averageRating: Double, val votesAmount: Int)

data class ContentInfo(
    val cinematographicContent: CinematographicContent?, val platform: String,
    val seasonNumber: Int? = null, val episodeNumber: Int? = null
)

data class ReviewInfo(
    val resumeText: String, val text: String, val rating: Double, val date: LocalDateTime,
    val reviewType: ReviewType, val language: String, val geographicLocation: String
)

data class PublicReviewInfo(
    val includeSpoiler: Boolean,
    val username: String,
    val userId: String
)

data class SerieInfo(val endYear: Int?, val seasons: List<Season>)


@Entity
class ValorationData(
    @ManyToOne(fetch = FetchType.EAGER)
    var review: Review,
    var userId: String,
    var platform: String,
    var valoration: Valoration,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun isFromUser(userId: String, platform: String) = this.userId == userId && this.platform == platform
}

@Entity
class Report(
    val userId: String,
    val userPlatform: String,
    var reportType: ReportType
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun isFromUser(userId: String, userPlatform: String) = this.userId == userId && this.userPlatform == userPlatform

}