package ar.edu.unq.grupoN.backenddesappapi.model.critics

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn


@Entity
@PrimaryKeyJoinColumn(name="id")
class Public(
    cinematographicContent: CinematographicContent,
    resumeText: String,
    text: String,
    rating: Rating,
    date: LocalDateTime,
    platform: String,
    val includeSpoiler: Boolean,
    val userId: String,
    val username: String,
    val language: String,
    val geographicLocation: String,
    isAChapterReview: IsAChapterReview,
    seasonNumber: Int? = null,
    episodeNumber: Int? = null
): Review(cinematographicContent, resumeText, text, rating,
    date, platform, isAChapterReview, seasonNumber, episodeNumber)