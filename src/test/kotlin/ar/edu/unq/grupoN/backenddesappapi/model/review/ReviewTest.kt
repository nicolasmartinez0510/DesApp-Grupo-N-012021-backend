package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.Factory
import ar.edu.unq.grupoN.backenddesappapi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime


class ReviewTest {
    val factory = Factory()

    @Test
    fun `a new public review on a serie episode`(){
        val review = factory.publicReviewOn(factory.spartacusSerie(), 4.0, ReviewType.CHAPTER,2,6)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.spartacusSerie())
        assertThat(review.episodeNumber).isEqualTo(6)
        assertThat(review.seasonNumber).isEqualTo(2)
        assertThat(review.resumeText).isEqualTo(resumeText)
        assertThat(review.text).isEqualTo(text)
        assertThat(review.rating).isEqualTo(4.0)
        assertThat(review.includeSpoiler).isTrue
        assertThat(review.date).isEqualTo(date)
        assertThat(review.platform).isEqualTo(platform)
        assertThat(review.userId).isEqualTo(userId)
        assertThat(review.reviewType).isEqualTo(ReviewType.CHAPTER)
        assertThat(review.language).isEqualTo(languague)
        assertThat(review.username).isEqualTo(username)
        assertThat(review.geographicLocation).isEqualTo(geographicLocation)
    }

    @Test
    fun `a public review set public review info`(){
        val oldReview = factory.publicReviewOn(factory.spartacusSerie(), 4.0, ReviewType.CHAPTER,2,6)
        val review = factory.publicReviewOn(factory.spartacusSerie(), 4.0, ReviewType.CHAPTER,2,6)

        review.geographicLocation = "Guadalajara"
        review.language = "Spanish"
        review.userId = "AnotherId"
        review.username = "AnotherUsername"
        review.includeSpoiler = false

        assertThat(review.geographicLocation).isNotEqualTo(oldReview.geographicLocation)
        assertThat(review.language).isNotEqualTo(oldReview.language)
        assertThat(review.userId).isNotEqualTo(oldReview.userId)
        assertThat(review.username).isNotEqualTo(oldReview.username)
        assertThat(review.includeSpoiler).isEqualTo(!oldReview.includeSpoiler)
    }
    @Test
    fun `a new public review on a movie`(){
        val review = factory.publicReviewOn(factory.gladiatorMovie(), 3.0, ReviewType.MOVIE)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.gladiatorMovie())
        assertThat(review.reviewType).isEqualTo(ReviewType.MOVIE)
        assertThat(review.isPublic).isTrue
    }

    @Test
    fun `a new premium review on a serie know his data and who is not a public review`(){
        val review = factory.premiumReviewOn(factory.spartacusSerie(), 1.5, ReviewType.SERIE)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.spartacusSerie())
        assertThat(review.reviewType).isEqualTo(ReviewType.SERIE)
        assertThat(review.reviewerId).isEqualTo(reviewerId)
        assertThat(review.isPublic  ).isFalse
    }

    @Test
    fun `a new premium review on a movie`(){
        val review = factory.premiumReviewOn(factory.gladiatorMovie(), 5.0, ReviewType.MOVIE)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.gladiatorMovie())
        assertThat(review.reviewType).isEqualTo(ReviewType.MOVIE)
        assertThat(review.reviewerId).isEqualTo(reviewerId)
    }

    @Test
    fun `a public new review receive two dislikes and now have less rating`() {
        val review = factory.aPublicReview()
        val valorationBeforeReceiveValorations = review.valoration
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.DISLIKE)
        review.rate(userId = "Fede", platform = "Netflix",valoration= Valoration.DISLIKE)

        val valorationAfterReceiveValorations = valorationBeforeReceiveValorations - 2
        assertThat(review.valoration).isEqualTo(valorationAfterReceiveValorations)
    }

    @Test
    fun `a user from a platform who like two times a review count by one`() {
        val review = factory.aPublicReview()
        val valorationBeforeReceiveValorations = review.valoration
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.LIKE)
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.LIKE)

        val valorationAfterReceiveValorations = valorationBeforeReceiveValorations + 1
        assertThat(review.valoration).isEqualTo(valorationAfterReceiveValorations)
    }

    @Test
    fun `when a user from a platform who like and after dislike a review, review have one valoration point less`() {
        val review = factory.aPublicReview()
        val valorationBeforeReceiveValorations = review.valoration
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.LIKE)
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.DISLIKE)

        val valorationAfterReceiveValorations = valorationBeforeReceiveValorations - 1
        assertThat(review.valoration).isEqualTo(valorationAfterReceiveValorations)
    }

    @Test
    fun `throws an exception when validate a review with movie type, but his content is a serie`(){
        val review = factory.publicReviewOn(factory.spartacusSerie(), 4.0, ReviewType.MOVIE)

        val exception = assertThrows<RuntimeException> { review.validate() }
        assertThat("Invalid review type, 'Spartacus' is a Serie").isEqualTo(exception.message)
    }


    @Test
    fun `throws an exception when validate a review with serie type, but his content is a movie`(){
        val review = factory.publicReviewOn(factory.gladiatorMovie(), 4.0, ReviewType.SERIE)

        val exception = assertThrows<InvalidReviewTypeException> { review.validate() }
        assertThat("Invalid review type, 'Gladiator' is a Movie").isEqualTo(exception.message)
    }

    @Test
    fun `throws an exception when validate a review with chapter type, but his content is a movie`(){
        val review = factory.publicReviewOn(factory.gladiatorMovie(), 4.0, ReviewType.CHAPTER)

        val exception = assertThrows<InvalidReviewTypeException> { review.validate() }
        assertThat("Invalid review type, 'Gladiator' is a Movie").isEqualTo(exception.message)
    }

    @Test
    fun `throws an exception when validate a review with chapter type, but episode doesn't exist`(){
        val review = factory.publicReviewOn(factory.spartacusSerie(), 4.0, ReviewType.CHAPTER,2,6)

        val exception = assertThrows<DoesNotExistChapterException> { review.validate() }
        assertThat("This chapter doesn't exist in 'Spartacus' yet").isEqualTo(exception.message)
    }

    @Test
    fun `throws an exception when validate a review with chapter type, but episode or season number is null`(){
        val review = factory.publicReviewOn(factory.spartacusSerie(), 4.0, ReviewType.CHAPTER)

        val exception = assertThrows<InvalidSeasonOrEpisodeNumberException> { review.validate() }
        assertThat("Invalid season or episode number, in a chapter review, both must be a number").isEqualTo(exception.message)
    }

    @Test
    fun `can reported an offensive review`(){
        val review = factory.aPublicReview()

        review.report("Chester", "NETFLIX", ReportType.OFFENSIVE)

        assertThat(review.reports.size).isEqualTo(1)
        assertThat(review.reports[0].reportType).isEqualTo(ReportType.OFFENSIVE)
    }

    @Test
    fun `a user who report a review two times the last report only have effect`(){
        val review = factory.aPublicReview()

        review.report("Chester", "NETFLIX", ReportType.OFFENSIVE)
        review.report("Chester", "NETFLIX", ReportType.SPAM)

        assertThat(review.reports.size).isEqualTo(1)
        assertThat(review.reports[0].reportType).isEqualTo(ReportType.SPAM)
    }

    @Test
    fun `cannot create a review with a rating above five`(){

        val exception = assertThrows<InvalidReviewRatingException> { factory.publicReviewOn(factory.spartacusSerie(),5.15, ReviewType.SERIE) }

        assertThat(exception.message).isEqualTo("Invalid review rating")
    }

    @Test
    fun `cannot create a review with a rating below zero`(){

        val exception = assertThrows<InvalidReviewRatingException> { factory.publicReviewOn(factory.spartacusSerie(),-1.0, ReviewType.SERIE) }

        assertThat(exception.message).isEqualTo("Invalid review rating")
    }

    private val resumeText = "Lorem Ipsum has been the industry's standard dummy"
    private val text = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
    private val date = LocalDateTime.of(2018,10,15,23,16)
    private val platform = "Netflix"
    private val userId = "chester44"
    private val username = "chesterfield"
    private val geographicLocation = "United States"
    private val languague = "English"
    private val reviewerId = "ASDFfktuyPTi9r8rY"

}