package ar.edu.unq.grupoN.backenddesappapi

import ar.edu.unq.grupoN.backenddesappapi.model.critics.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.*
import java.time.LocalDateTime

class Factory {

    fun public_review_on(cinematographicContent: CinematographicContent, rating: Rating,
                         isAChapterReview: IsAChapterReview, seasonNumber:Int? = null, episodeNumber:Int? = null): Public{
        return if (cinematographicContent.isSerie()){
            val serie: Serie = cinematographicContent as Serie
            Public(serie, resumeText, text, rating,
                date, platform, includeSpoiler, userId, username, language, geographicLocation,
                isAChapterReview, seasonNumber, episodeNumber)
        } else {
            val movie: Movie = cinematographicContent as Movie
            Public(movie, resumeText, text, rating,
                date, platform,  includeSpoiler, userId, username, language, geographicLocation,
                isAChapterReview)
        }
    }

    fun premium_review_on(cinematographicContent: CinematographicContent,
                          rating: Rating, isAChapterReview: IsAChapterReview, seasonNumber:Int? = null, episodeNumber:Int? = null): Premium{
        val reviewerId = "ASDFfktuyPTi9r8rY"
        return if (cinematographicContent.isSerie()){
            val serie: Serie = cinematographicContent as Serie
            Premium(serie, resumeText, text, rating,
                date, platform, reviewerId, isAChapterReview, seasonNumber, episodeNumber)
        } else {
            val movie: Movie = cinematographicContent as Movie
            Premium(movie, resumeText, text, rating,
                date, platform, reviewerId, isAChapterReview)
        }
    }

    fun spartacus_serie(): Serie {
        val seasons = Season(2, listOf(Episode(1)))

        return Serie("SPTKSukq4sk893", ordering, "Spartacus", region, language, types, attributes,
                isOriginalTitle, titleType, "Spartacus", "Spartacus", isAdult, 2010, runtimeMinutes,
                genres, directors, writers, actors, averageRating, numVotes, 2013, listOf(seasons))
    }

    fun gladiator_movie(): Movie {
        return Movie(titleId, ordering, title, region, language, types, attributes,
            isOriginalTitle, titleType, primaryTitle, originalTitle, isAdult, startYear, runtimeMinutes,
            genres, directors, writers, actors, averageRating, numVotes)
    }

    fun genericCastMember() = CastMember("Russell Crowe", "Actor",
        null, "Maximus", 9999, 9999, listOf())


    //Objects variables

    val titleId = "GLD320jskiQ"
    val ordering = 3
    val title = "Gladiator"
    val region = "USA"
    val language = "English"
    val types = listOf("alternative", "tv")
    val attributes = listOf("violence", "roma", "history")
    val isOriginalTitle = true
    val titleType = "movie"
    val primaryTitle = "Gladiator"
    val originalTitle = "Gladiator"
    val isAdult = true
    val startYear = 2000
    val runtimeMinutes = 155
    val genres = listOf("action", "adventure", "drama")
    val directors = listOf(genericCastMember())
    val writers = listOf(genericCastMember(), genericCastMember(), genericCastMember())
    val averageRating = 4.9
    val numVotes = 1355421
    val actors = listOf(genericCastMember())

    val resumeText = "Lorem Ipsum has been the industry's standard dummy"
    val text = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
    val includeSpoiler = true
    val date = LocalDateTime.of(2018,10,15,23,16)
    val platform = "Netflix"
    val userId = "nockkO^4"
    val username = "chesterfield"
    val geographicLocation = "United States"
}