package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import ar.edu.unq.grupoN.backenddesappapi.model.*
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.*
import ar.edu.unq.grupoN.backenddesappapi.model.review.Premium
import ar.edu.unq.grupoN.backenddesappapi.model.review.Public
import ar.edu.unq.grupoN.backenddesappapi.service.CinematographicContentService
import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.RegisterRequest
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import com.github.javafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Configuration
class FakeDataConfiguration {
    val faker = Faker()

    @Autowired
    private lateinit var cinematographicContentService: CinematographicContentService

    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var adminPlatformService: PlatformAdminService

    @Bean
    fun fakeDataInject() =
        CommandLineRunner {
            val amountOfEachContent = faker.number().numberBetween(4, 6)

            createDataToSwagger(cinematographicContentService, reviewService)
            createMovies(amountOfEachContent, cinematographicContentService, reviewService)
            createSeries(amountOfEachContent, cinematographicContentService, reviewService)

            adminPlatformService.register(
                RegisterRequest(
                    "chester", Platform.PLEX,
                    "resenia.grupon.desapp2021s1@gmail.com", "1234"
                )
            )
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
                    cast = getCastMembers(faker.number().numberBetween(5, 8)),
                )

            cinematographicContentService.add(newMovie)

            generateReviews(
                cantidad = faker.number().numberBetween(2, 4),
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
                rating = faker.number().randomDouble(2, 1, 5),
                date = faker.date().past(3000, TimeUnit.DAYS)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                language = faker.options().option(Language::class.java).toString(),
                geographicLocation = faker.options().option(Country::class.java).toString(),
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
                userId = username + faker.number().numberBetween(0, 500000)
            )

            val contentInfo = ContentInfo(
                cinematographicContent = content,
                platform = faker.options().option(Platform::class.java).toString()
            )

            val publicReview = Public(contentInfo, reviewInfo, publicReviewInfo)

            repeat(faker.number().numberBetween(1, 6)) {
                publicReview.rate(
                    faker.name().username(),
                    faker.options().option(Platform::class.java).toString(),
                    faker.options().option(Valoration::class.java)
                )
            }

            val premiumReview = Premium(contentInfo, reviewInfo, faker.rickAndMorty().character() + "Id")
            repeat(faker.number().numberBetween(2, 4)) {
                premiumReview.rate(
                    faker.name().username(),
                    faker.options().option(Platform::class.java).toString(),
                    faker.options().option(Valoration::class.java)
                )
            }

            reviewService.saveReview(content.titleId, ReviewDTO.fromModel(publicReview))
            reviewService.saveReview(content.titleId, ReviewDTO.fromModel(premiumReview))

        }
    }

    private fun createDataToSwagger(
        cinematographicContentService: CinematographicContentService,
        reviewService: ReviewService
    ) {
        val cast = getCastMembers(faker.number().numberBetween(5, 8))
        cast.add(CastMember("Chestersaurio", Employment.ACTOR, "NOTHING", "Pepe", 1999, null))
        val gladiatorMovie =
            Movie(
                basicInformation = getBasicInformation(),
                cast = cast,
            )

        val reviewInfo = ReviewInfo(
            resumeText = faker.lorem().sentence(),
            text = "TEXT " + faker.lorem().sentence(),
            rating = faker.number().randomDouble(2, 1, 5),
            date = faker.date().past(3000, TimeUnit.DAYS)
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            language = "ENGLISH",
            geographicLocation = "ARGENTINA",
            reviewType = ReviewType.MOVIE
        )
        val username = faker.name().username()
        val publicReviewInfo = PublicReviewInfo(
            includeSpoiler = false,
            username = username,
            userId = username + faker.number().numberBetween(0, 500000)
        )

        val contentInfo = ContentInfo(
            cinematographicContent = gladiatorMovie,
            platform = "NETFLIX"
        )
        val publicReview = Public(contentInfo, reviewInfo, publicReviewInfo)
        gladiatorMovie.titleId = "GladiatorID"
        gladiatorMovie.title = "Gladiator"
        publicReview.id = 1

        cinematographicContentService.add(gladiatorMovie)
        reviewService.saveReview(gladiatorMovie.titleId, ReviewDTO.fromModel(publicReview))

    }
}