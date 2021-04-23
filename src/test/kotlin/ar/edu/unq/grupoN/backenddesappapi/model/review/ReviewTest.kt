package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.Factory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class ReviewTest {
    val factory = Factory()

    @Test
    fun `a new public review on a serie episode`(){
        val review = factory.public_review_on(factory.spartacus_serie(), Rating.FOUR, IsAChapterReview.YES,2,6)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.spartacus_serie())
        assertThat(review.episodeNumber).isEqualTo(6)
        assertThat(review.seasonNumber).isEqualTo(2)
        assertThat(review.resumeText).isEqualTo(resumeText)
        assertThat(review.text).isEqualTo(text)
        assertThat(review.rating).isEqualTo(Rating.FOUR)
        assertThat(review.includeSpoiler).isTrue()
        assertThat(review.date).isEqualTo(date)
        assertThat(review.platform).isEqualTo(platform)
        assertThat(review.userId).isEqualTo(userId)
        assertThat(review.isAChapterReview).isEqualTo(IsAChapterReview.YES)
        assertThat(review.language).isEqualTo(languague)
        assertThat(review.username).isEqualTo(username)
        assertThat(review.geographicLocation).isEqualTo(geographicLocation)
    }

    @Test
    fun `a public review set public review info`(){
        val oldReview = factory.public_review_on(factory.spartacus_serie(), Rating.FOUR, IsAChapterReview.YES,2,6)
        val review = factory.public_review_on(factory.spartacus_serie(), Rating.FOUR, IsAChapterReview.YES,2,6)

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
        val review = factory.public_review_on(factory.gladiator_movie(), Rating.THREE, IsAChapterReview.ISAMOVIE)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.gladiator_movie())
        assertThat(review.isAChapterReview).isEqualTo(IsAChapterReview.ISAMOVIE)
    }

    @Test
    fun `a new premium review on a serie`(){
        val review = factory.premium_review_on(factory.spartacus_serie(), Rating.ONE, IsAChapterReview.NO)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.spartacus_serie())
        assertThat(review.isAChapterReview).isEqualTo(IsAChapterReview.NO)
        assertThat(review.reviewerId).isEqualTo(reviewerId)
    }

    @Test
    fun `a new premium review on a movie`(){
        val review = factory.premium_review_on(factory.gladiator_movie(), Rating.FIVE, IsAChapterReview.ISAMOVIE)

        assertThat(review.cinematographicContent).usingRecursiveComparison()
            .isEqualTo(factory.gladiator_movie())
        assertThat(review.isAChapterReview).isEqualTo(IsAChapterReview.ISAMOVIE)
        assertThat(review.reviewerId).isEqualTo(reviewerId)
    }

    @Test
    fun `a public new review recieve a like and now have a one like more`(){
        val review = factory.a_public_review()
        review.rate(userId = "Nico", platform = "Netflix" ,valoration= Valoration.LIKE)

        assertThat(review.amountOf(Valoration.LIKE)).isEqualTo(1)

    }

    @Test
    fun `a public new review receive a dislike and now have a one dislike more`() {
        val review = factory.a_public_review()
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.DISLIKE)

        assertThat(review.amountOf(Valoration.DISLIKE)).isEqualTo(1)
    }

    @Test
    fun `a public new review receive two dislikes and now have less rating`() {
        val review = factory.a_public_review()
        val valorationBeforeReceiveValorations = review.valoration()
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.DISLIKE)
        review.rate(userId = "Fede", platform = "Netflix",valoration= Valoration.DISLIKE)

        val valorationAfterReceiveValorations = valorationBeforeReceiveValorations - 2
        assertThat(review.valoration()).isEqualTo(valorationAfterReceiveValorations)
    }

    @Test
    fun `a user from a platform who like two times a review count by one`() {
        val review = factory.a_public_review()
        val valorationBeforeReceiveValorations = review.valoration()
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.LIKE)
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.LIKE)

        val valorationAfterReceiveValorations = valorationBeforeReceiveValorations + 1
        assertThat(review.valoration()).isEqualTo(valorationAfterReceiveValorations)
    }

    @Test
    fun `when a user from a platform who like and after dislike a review, review only has a dislike`() {
        val review = factory.a_public_review()
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.LIKE)
        review.rate(userId = "Nico", platform = "Netflix",valoration= Valoration.DISLIKE)

        assertThat(review.amountOf(Valoration.DISLIKE)).isEqualTo(1)
        assertThat(review.amountOf(Valoration.LIKE)).isEqualTo(0)
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