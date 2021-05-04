package ar.edu.unq.grupoN.backenddesappapi

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.review.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.*
import java.time.LocalDateTime

class Factory {

    fun aPublicReview(): Public{
        val reviewInfo = ReviewInfo(resumeText, text, Rating.THREE, date, ReviewType.MOVIE, language, geographicLocation)
        val publicReviewInfo = PublicReviewInfo(includeSpoiler, username, userId)
        val contentInfo = ContentInfo(gladiatorMovie(), platform)
        return Public(contentInfo, reviewInfo, publicReviewInfo)
    }

    fun publicReviewOn(
        cinematographicContent: CinematographicContent, rating: Rating,
        reviewType: ReviewType, seasonNumber: Int? = null, episodeNumber: Int? = null
    ): Public {
        val reviewInfo = ReviewInfo(resumeText, text, rating, date, reviewType, language, geographicLocation)
        val publicReviewInfo = PublicReviewInfo(includeSpoiler, username, userId)

        return if (cinematographicContent.isSerie()) {
            val serie: Serie = cinematographicContent as Serie
            val contentInfo = ContentInfo(serie, platform, seasonNumber, episodeNumber)
            Public(contentInfo, reviewInfo, publicReviewInfo)
        } else {
            val movie: Movie = cinematographicContent as Movie
            val contentInfo = ContentInfo(movie, platform)
            Public(contentInfo, reviewInfo, publicReviewInfo)
        }
    }

    fun premiumReviewOn(
        cinematographicContent: CinematographicContent,
        rating: Rating, reviewType: ReviewType, seasonNumber: Int? = null, episodeNumber: Int? = null
    ): Premium {
        val reviewInfo = ReviewInfo(resumeText, text, rating, date, reviewType, language, geographicLocation)
        val reviewerId = "ASDFfktuyPTi9r8rY"
        return if (cinematographicContent.isSerie()) {
            val serie: Serie = cinematographicContent as Serie
            val contentInfo = ContentInfo(serie, platform, seasonNumber, episodeNumber)
            Premium(contentInfo, reviewInfo, reviewerId)
        } else {
            val movie: Movie = cinematographicContent as Movie
            val contentInfo = ContentInfo(movie, platform, seasonNumber, episodeNumber)
            Premium(contentInfo, reviewInfo, reviewerId)
        }
    }

    fun spartacusSerie(): Serie {
        val episode = Episode(1)
        episode.id = 3
        val seasons = Season(2, listOf(episode))
        seasons.id = 23
        val basicInformation = BasicInformation(
            "SPTKSukq4sk893", "Spartacus",
            titleType, isAdult, 2010, runtimeMinutes
        )
        val rating = RatingInfo(averageRating, numVotes)
        val serieInfo = SerieInfo(2013, listOf())
        val spartacus = Serie(basicInformation, cast, rating, serieInfo)
        spartacus.seasons = listOf(seasons)
        spartacus.endYear = 2013

        return spartacus
    }

    fun gladiatorMovie(): Movie {
        val basicInformation = BasicInformation(
            "GLADIIiiatoor45", "Gladiator",
            titleType, isAdult, startYear, runtimeMinutes
        )
        val rating = RatingInfo(averageRating, numVotes)

        return Movie(basicInformation, cast, rating)
    }

    fun genericCastMember(): CastMember {
        val russell = CastMember(
            "Russell Crowe", Employment.ACTOR,
            null, "Maximus", 9999, 9999
        )
        return russell
    }

    //Objects atibutes
    val language = "English"
    val titleType = "movie"
    val isAdult = true
    val startYear = 2000
    val runtimeMinutes = 155
    val cast = mutableListOf(genericCastMember())
    val averageRating = 4.9
    val numVotes = 1355421

    val resumeText = "Lorem Ipsum has been the industry's standard dummy"
    val text =
        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
    val includeSpoiler = true
    val date = LocalDateTime.of(2018, 10, 15, 23, 16)
    val platform = "Netflix"
    val userId = "chester44"
    val username = "chesterfield"
    val geographicLocation = "United States"
}