package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.Factory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CinematographicContentTest {
    lateinit var gladiator : CinematographicContent
    lateinit var spartacus : Serie
    val factory = Factory()

    @BeforeEach
    fun setUp(){
        spartacus = factory.spartacusSerie()
        gladiator = factory.gladiatorMovie()
    }

    @Test
    fun `a cinematographic content know all IMDb information`(){
        assertThat(gladiator).usingRecursiveComparison()
            .isEqualTo(factory.gladiatorMovie())
        assertThat(gladiator.isAdultContent).isTrue()
    }

    @Test
    fun `series have extra information about IMDb information`(){
        val season = a_season()

        assertThat(spartacus.endYear).isEqualTo(2013)
        assertThat(spartacus.seasons).usingRecursiveComparison()
            .isEqualTo(listOf(season))
    }

    @Test
    fun `a content can be rated by a review and obtain one more votes amount`(){
        val votesBeforeRate = gladiator.votesAmount

        gladiator.addRate(factory.aPublicReview())

        assertThat(gladiator.votesAmount).isEqualTo(votesBeforeRate + 1)
    }

    @Test
    fun `a content can be rated by a review have new rating and sum votes`(){
        val sumVotesBeforeRate = gladiator.sumVotes
        val ratingBeforeRate = gladiator.sumVotes

        gladiator.addRate(factory.aPublicReview())
        gladiator.addRate(factory.aPublicReview())

        assertThat(gladiator.sumVotes).isEqualTo(sumVotesBeforeRate + 3 + 3)
        assertThat(gladiator.averageRating).isEqualTo(ratingBeforeRate + 3)
    }

    private fun a_season(): Season {
        val episode = Episode(1)
        episode.id = 3
        val season = Season(2, listOf(episode))
        season.id = 23
        return season
    }
}