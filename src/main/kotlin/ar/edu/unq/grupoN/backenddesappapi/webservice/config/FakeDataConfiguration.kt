package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.*
import ar.edu.unq.grupoN.backenddesappapi.model.review.*
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.CinematographicContentService
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import com.github.javafaker.Faker
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Configuration
class FakeDataConfiguration {
    val faker = Faker()

    @Bean
    fun fakeMoviesAndSeriesInject(
        cinematographicContentRepository: CinematographicContentRepository,
        reviewRepository: ReviewRepository
    ) =
        CommandLineRunner {
            val cinematographicContentService = CinematographicContentService(cinematographicContentRepository)
            val reviewService = ReviewService(reviewRepository)
            val amountOfEachContent = faker.number().numberBetween(4,6)

            createMovies(amountOfEachContent, cinematographicContentService, reviewService)
            createSeries(amountOfEachContent, cinematographicContentService, reviewService)

        }

    // private auxiliar functions for generate fake data.
    private fun createSeries(
        amount: Int,
        cinematographicContentService: CinematographicContentService,
        reviewService: ReviewService
    ) {
        repeat(amount) {
            val serieInfo =
                SerieInfo(
                    endYear = faker.number().numberBetween(2000, 2021),
                    seasons = mutableListOf(Season(1, mutableListOf(Episode(1), Episode(2))))
                )

            val serie =
                Serie(
                    basicInformation = getBasicInformation(),
                    cast = getCastMembers(5),
                    rating = getRatingInfo(),
                    serieInfo = serieInfo
                )

            cinematographicContentService.add(serie)

            generateReviews(
                cantidad = 2,
                content = serie,
                reviewService = reviewService
            )
        }
    }

    private fun createMovies(
        amount: Int,
        cinematographicContentService: CinematographicContentService,
        reviewService: ReviewService
    ) {
        repeat(amount) {
            val newMovie =
                Movie(
                    basicInformation = getBasicInformation(),
                    cast = getCastMembers(faker.number().numberBetween(5,8)),
                    rating = getRatingInfo()
                )

            cinematographicContentService.add(newMovie)

            generateReviews(
                cantidad = faker.number().numberBetween(2,4),
                content = newMovie,
                reviewService = reviewService
            )
        }
    }


    private fun getBasicInformation(): BasicInformation {
        val title = faker.book().title()
        return BasicInformation(
            titleId = (title + "-" + faker.code().isbn10() + "-" + "id").replace("\\s".toRegex(), ""),
            title = title,
            titleType = "Adventure",
            isAdultContent = faker.bool().bool(),
            startYear = faker.number().numberBetween(1990, 2000),
            runtimeMinutes = faker.number().numberBetween(100, 1000)
        )
    }

    private fun getCastMembers(amount: Int): MutableList<CastMember> {
        val cast: MutableList<CastMember> = mutableListOf()

        repeat(amount) {
            cast.add(
                genericCastMember(
                    name = faker.name().fullName(),
                    employment = faker.options().option(Employment::class.java)
                )
            )
        }
        return cast
    }

    private fun getRatingInfo() = RatingInfo(
        averageRating = faker.number().randomDouble(2, 3, 10),
        votesAmount = faker.number().numberBetween(2, 9999989)
    )

    private fun genericCastMember(name: String, employment: Employment): CastMember {
        return CastMember(
            name = name,
            employment = employment,
            job = null,
            character = faker.funnyName().name(),
            birthYear = faker.number().numberBetween(1850, 1986),
            deathYear = faker.number().numberBetween(2005, 2021)
        )
    }

    private fun generateReviews(cantidad: Int, content: CinematographicContent, reviewService: ReviewService) {
        repeat(cantidad) {
            val reviewInfo = ReviewInfo(
                resumeText = faker.lorem().sentence(),
                text = "TEXT " + faker.lorem().sentence(),
                rating = faker.options().option(Rating::class.java),
                date = faker.date().past(3000, TimeUnit.DAYS)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                language = faker.options().option(Language::class.java).toString(),
                reviewType =
                if (content.isSerie()) {
                    ReviewType.SERIE
                } else {
                    ReviewType.MOVIE
                }
            )
            val username = faker.name().username()
            val publicReviewInfo = PublicReviewInfo(
                includeSpoiler = faker.bool().bool(),
                username = username,
                userId = username + faker.number().numberBetween(0, 500000),
                geographicLocation = faker.address().country()
            )

            val contentInfo = ContentInfo(
                cinematographicContent = content,
                platform = faker.options().option(Platform::class.java).toString()
            )

           val public_review = Public(contentInfo, reviewInfo, publicReviewInfo)

            repeat(faker.number().numberBetween(1,3)){
                public_review.rate(faker.name().username(),
                    faker.options().option(Platform::class.java).toString(),
                    faker.options().option(Valoration::class.java))
            }

            val premium_review = Premium(contentInfo,reviewInfo, faker.rickAndMorty().character() +"Id")
            repeat(faker.number().numberBetween(3,4)){
                premium_review.rate(faker.name().username(),
                    faker.options().option(Platform::class.java).toString(),
                    faker.options().option(Valoration::class.java))
            }

            reviewService.add(public_review)
            reviewService.add(premium_review)
        }
    }

    private enum class Platform {
        NETFLIX, AMAZON, PLEX, DISNEY
    }

    private enum class Language {
        ENGLISH, FRENCH, SPANISH, PORTUGUESE, LATIN, ARAMIC, RUSSIAN
    }

}