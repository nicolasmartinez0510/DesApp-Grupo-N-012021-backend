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
import ar.edu.unq.grupoN.backenddesappapi.service.ReviewService
import com.github.javafaker.Faker
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

@Configuration
class FakeDataConfiguration {
    // faker object
    val faker = Faker()


    @Bean
    fun fakeMoviesAndSeriesInject(cinematographicContentRepository: CinematographicContentRepository, reviewRepository: ReviewRepository) =
        CommandLineRunner {
            val cinematographicContentService = CinematographicContentService(cinematographicContentRepository)
            val reviewService = ReviewService(reviewRepository)

            crearPelis(4,cinematographicContentService,reviewService)
            crearSeries(4,cinematographicContentService,reviewService)

        }

    // Genera un set de series
    private fun crearSeries(cantidad: Int, cinematographicContentService: CinematographicContentService, reviewService: ReviewService)
    {
        repeat(cantidad){
            val serieInfo =
                SerieInfo(
                    endYear = faker.number().numberBetween(2000,2021),
                    seasons = mutableListOf(Season(1,mutableListOf(Episode(2))))
                )

            val serie =
                Serie(
                    basicInformation = getBasicInformation(),
                    cast = getCastMembers(5),
                    rating = getRatingInfo(),
                    serieInfo
                )

            cinematographicContentService.add(serie)

            genereteReviews(
                cantidad = 2,
                content = serie,
                reviewService = reviewService
            )
        }
    }

    // Genera un set de peliculas
    private fun crearPelis(cantidad:Int,
                           cinematographicContentService: CinematographicContentService ,
                           reviewService: ReviewService)
    {
       repeat(cantidad){
            val newMovie =
                Movie(
                    basicInformation = getBasicInformation(),
                    cast = getCastMembers(5),
                    rating = getRatingInfo()
                )

           cinematographicContentService.add(newMovie)

           genereteReviews(
               cantidad = 2,
               content = newMovie,
               reviewService = reviewService
           )
        }
    }


    // Funciones auxiliares
    private fun getBasicInformation(): BasicInformation {
        val title = faker.lorem().word()
        return BasicInformation(
            titleId = title + "-" + faker.number().numberBetween(0,80000) + "-" + "ID",
            title = title,
            titleType = "Adventure",
            isAdultContent = faker.bool().bool(),
            startYear = faker.number().numberBetween(1990, 2000),
            runtimeMinutes = faker.number().numberBetween(100, 1000)
        )
    }

    private fun getCastMembers(cantidad: Int): MutableList<CastMember> {
        val cast: MutableList<CastMember> = mutableListOf()

        repeat(cantidad){
            cast.add(genericCastMember(
                name = faker.name().fullName(),
                employment = faker.options().option(Employment::class.java)
            ))
        }
        return cast
    }

    private fun getRatingInfo() = RatingInfo(
        averageRating = faker.number().randomDouble(2, 0, 10),
        votesAmount = faker.number().numberBetween(0, 100000)
    )

    private fun genericCastMember(name: String, employment: Employment): CastMember {
        return CastMember(
            name = name,
            employment = employment,
            job = null,
            character = faker.funnyName().name(),
            birthYear = faker.number().numberBetween(1850,2010),
            deathYear = faker.number().numberBetween(2010,2021)
        )
    }

   private fun genereteReviews(cantidad: Int,content: CinematographicContent,reviewService: ReviewService) {
      repeat(cantidad) {
          val reviewInfo = ReviewInfo(
              resumeText = faker.lorem().sentence(),
              text = faker.lorem().paragraph(),
              rating = faker.options().option(Rating::class.java),
              date = faker.date().past(3000,TimeUnit.DAYS)
                  .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
              isAChapterReview = IsAChapterReview.ISAMOVIE
          )
          val username = faker.name().username()
          val publicReviewInfo = PublicReviewInfo(
              includeSpoiler = faker.bool().bool(),
              username = username,
              userId = username + faker.number().numberBetween(0,500000),
              language = faker.options().option(Idioms::class.java).toString(),
              geographicLocation = faker.address().country()
          )

          val contentInfo = ContentInfo(
              cinematographicContent = content,
              platform = faker.options().option(Platform::class.java).toString()
          )

          reviewService.add(Public(contentInfo, reviewInfo, publicReviewInfo))
      }
   }

    private enum class Platform{
        NETFLIX,AMAZON,PLEX,DISNEY
    }

    private enum class Idioms{
        ENGLISH,FRANCAIS,SPANISH,PORTUGUES,LATIN,ARAMEO,RUSO
    }

}