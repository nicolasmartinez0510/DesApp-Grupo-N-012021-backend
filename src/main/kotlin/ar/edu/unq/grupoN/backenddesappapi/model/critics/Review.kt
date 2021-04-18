package ar.edu.unq.grupoN.backenddesappapi.model.critics

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import java.time.LocalDateTime

open class Review(
    val cinematographicContent: CinematographicContent,
    val resumeText: String,
    val text: String,
    val rating: Rating,
    val date: LocalDateTime,
    val platform: String,
    val isAChapterReview: IsAChapterReview,
    val seasonNumber: Int?,
    val episodeNumber: Int?
)