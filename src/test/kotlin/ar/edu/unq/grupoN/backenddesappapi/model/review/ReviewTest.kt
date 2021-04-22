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