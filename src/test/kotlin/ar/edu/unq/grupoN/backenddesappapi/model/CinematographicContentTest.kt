package ar.edu.unq.grupoN.backenddesappapi.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CinematographicContentTest {
    lateinit var gladiator : Movie
    lateinit var spartacus : Serie
    private val genericCastMember: CastMember = genericCast()

    val titleId = "GLD320jskiQ"
    val ordering = 3
    val title = "Gladiator"
    val region = "USA"
    val language = "English"
    val types = listOf("alternative", "tv")
    val attributes = listOf("violence", "roma", "history")
    val isOriginalTitle = true
    val seasonNumber = 1
    val episodeNumber = 1
    val titleType = "movie"
    val primaryTitle = "Gladiator"
    val originalTitle = "Gladiator"
    val isAdult = true
    val startYear = 2000
    val endYear = null
    val runtimeMinutes = 155
    val genres = listOf("action", "adventure", "drama")
    val directors = listOf(genericCastMember)
    val writers = listOf(genericCastMember, genericCastMember, genericCastMember)
    val averageRating = 9.9
    val numVotes = 1355421
    val actors = listOf(genericCastMember)

    @BeforeEach
    fun setUp(){
        gladiator = Movie(titleId, ordering, title, region, language, types, attributes,
            isOriginalTitle, titleType, primaryTitle, originalTitle, isAdult, startYear, runtimeMinutes,
            genres, directors, writers, actors, averageRating, numVotes)

        spartacus = Serie(titleId, ordering, title, region, language, types, attributes,
            isOriginalTitle, titleType, primaryTitle, originalTitle, isAdult, startYear, runtimeMinutes,
            genres, directors, writers, actors, averageRating, numVotes, endYear, seasonNumber, episodeNumber)
    }
    @Test
    fun `a cinematographic content know all IMDb information`(){
        assertThat(gladiator.titleId).isEqualTo(titleId)
        assertThat(gladiator.ordering).isEqualTo(ordering)
        assertThat(gladiator.title).isEqualTo(title)
        assertThat(gladiator.region).isEqualTo(region)
        assertThat(gladiator.language).isEqualTo(language)
        assertThat(gladiator.types).containsExactly("alternative", "tv")
        assertThat(gladiator.attributes).containsExactly("violence", "roma", "history")
        assertThat(gladiator.isOriginalTitle).isTrue()
        assertThat(gladiator.titleType).isEqualTo(titleType)
        assertThat(gladiator.primaryTitle).isEqualTo(primaryTitle)
        assertThat(gladiator.originalTitle).isEqualTo(originalTitle)
        assertThat(gladiator.isAdult).isTrue()
        assertThat(gladiator.startYear).isEqualTo(startYear)
        assertThat(gladiator.runtimeMinutes).isEqualTo(runtimeMinutes)
        assertThat(gladiator.genres).containsExactly("action", "adventure", "drama")
        assertThat(gladiator.directors).containsExactly(genericCastMember)
        assertThat(gladiator.writers).containsExactly(genericCastMember,genericCastMember,genericCastMember)
        assertThat(gladiator.actors).containsExactly(genericCastMember)
        assertThat(gladiator.averageRating).isEqualTo(averageRating)
        assertThat(gladiator.numVotes).isEqualTo(numVotes)

    }

    @Test
    fun `series have extra information`(){
        assertThat(spartacus.endYear).isEqualTo(endYear)
        assertThat(spartacus.episodeNumber).isEqualTo(episodeNumber)
        assertThat(spartacus.seasonNumber).isEqualTo(seasonNumber)
    }

    private fun genericCast() = CastMember("Russell Crowe", "Actor",
        null, "Maximus", 9999, 9999, listOf())
}