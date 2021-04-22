package ar.edu.unq.grupoN.backenddesappapi.model

import ar.edu.unq.grupoN.backenddesappapi.model.review.IsAChapterReview
import ar.edu.unq.grupoN.backenddesappapi.model.review.Rating
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CastMember
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.Season
import java.time.LocalDateTime

data class BasicInformation(val titleId: String, val title: String, val titleType: String,
                            val isAdultContent: Boolean, val startYear: Int, val runtimeMinutes: Int)

data class Cast(val writers: List<CastMember>, val directors: List<CastMember>, val actors: List<CastMember>)

data class RatingInfo(val averageRating: Double, val votesAmount: Int)

data class ContentInfo(val cinematographicContent: CinematographicContent, val platform: String,
                       val seasonNumber: Int? = null, val episodeNumber: Int? = null)

data class ReviewInfo(val resumeText: String, val text: String, val rating: Rating, val date: LocalDateTime,
                      val isAChapterReview: IsAChapterReview)

data class PublicReviewInfo(
    val includeSpoiler: Boolean,
    val username: String,
    val userId: String,
    val language: String,
    val geographicLocation: String
)

data class SerieInfo(val endYear: Int?, val seasons: List<Season>)