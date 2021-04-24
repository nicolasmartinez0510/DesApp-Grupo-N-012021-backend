package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.*
import ar.edu.unq.grupoN.backenddesappapi.model.review.Employment
import ar.edu.unq.grupoN.backenddesappapi.model.review.IsAChapterReview
import ar.edu.unq.grupoN.backenddesappapi.model.review.Public
import ar.edu.unq.grupoN.backenddesappapi.model.review.Rating
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.CinematographicContentService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class FakeDataConfiguration {

    @Bean
    fun demo(cinematographicContentRepository: CinematographicContentRepository, reviewRepository: ReviewRepository) =
        CommandLineRunner {
            val cinematographicContentService = CinematographicContentService(cinematographicContentRepository)

            val movies = crearPelis()

            movies.forEach { movie -> cinematographicContentService.add(movie) }

            val basicInformation = BasicInformation("PataconesId", "Patacones", "Adventure",
            false, 2010, 155)
            val serieInfo = SerieInfo(2013, mutableListOf(Season(1,mutableListOf(Episode(2)))))
            val rating = RatingInfo(averageRating, numVotes)
            val serie = Serie(basicInformation, cast, rating, serieInfo)
            cinematographicContentService.add(serie)
            val reviewInfo = ReviewInfo(resumeText, text, Rating.THREE, date, IsAChapterReview.ISAMOVIE)
            val publicReviewInfo = PublicReviewInfo(includeSpoiler, username, userId,"English", geographicLocation)
            val contentInfo = ContentInfo(serie, platform)
            reviewRepository.save(Public(contentInfo, reviewInfo, publicReviewInfo))

        }

    private fun crearPelis(): List<CinematographicContent> {
        var i = 0
        val basicInformation = BasicInformation(
            "GladiatorId", "Gladiator",
            titleType, isAdult, startYear, runtimeMinutes
        )
        val rating = RatingInfo(averageRating, numVotes)

        val movies: MutableList<CinematographicContent> = mutableListOf()
        repeat(4){
            basicInformation.titleId = basicInformation.titleId + 1
            val newMovie = Movie(basicInformation,cast, rating)

            movies.add(newMovie)
            i += 4
        }

        return  movies
    }

    private fun genericCastMember(name: String, employment: Employment): CastMember {
        return CastMember(
            name, employment,
            null, "Maximus", 9999, 9999
        )
    }

    //Objects variables
    val titleType = "movie"
    val isAdult = true
    val startYear = 2000
    val runtimeMinutes = 155
    val cast = mutableListOf(genericCastMember("Alpachino", Employment.DIRECTOR),
        genericCastMember("J.K. Rowling", Employment.WRITER),
        genericCastMember("Russell Cowe", Employment.ACTOR),
        genericCastMember("La Rosalia", Employment.ACTOR))
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