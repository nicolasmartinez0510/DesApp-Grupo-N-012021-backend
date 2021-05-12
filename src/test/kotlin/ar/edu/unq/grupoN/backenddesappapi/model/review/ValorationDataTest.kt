package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.Factory
import ar.edu.unq.grupoN.backenddesappapi.model.Valoration
import ar.edu.unq.grupoN.backenddesappapi.model.ValorationData
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.Episode
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.Season
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ValorationDataTest {

    @Test
    fun `a valoration data knew his attributes afters set all`(){
        val anotherReview = Factory().aPublicReview()
        val valorationData = ValorationData(anotherReview,"ChestId","Netflix", Valoration.LIKE)
        valorationData.review = Factory().aPublicReview()
        valorationData.userId = "AnotherId"
        valorationData.platform = "Amazon"
        valorationData.valoration = Valoration.DISLIKE
        valorationData.id = 78

        assertThat(valorationData.review).isNotEqualTo(anotherReview)
        assertThat(valorationData.userId).isEqualTo("AnotherId").isNotEqualTo("ChestId")
        assertThat(valorationData.platform).isEqualTo("Amazon").isNotEqualTo("Netflix")
        assertThat(valorationData.valoration).isEqualTo(Valoration.DISLIKE).isNotEqualTo(Valoration.LIKE)
        assertThat(valorationData.id).isNotNull()
    }

    @Test
    fun `a valoration data know if is from a specific user`(){
        val valoration = valorationFrom("Chestersaurio", "Disney +")

        assertThat(valoration.isFromUser("Chestersaurio","Netflix")).isFalse
    }

    @Test
    fun `a season is not a season and has not a episode`(){
        val season = Season(1,listOf(Episode(1)))
        assertThat(season.haveEpisode(5)).isFalse
        assertThat(season.isSeason(2)).isFalse
    }

    @Test
    fun `a season is a season and has a episode`(){
        val season = Season(1,listOf(Episode(1)))
        assertThat(season.haveEpisode(1)).isTrue
        assertThat(season.isSeason(1)).isTrue
    }

    private fun valorationFrom(userId: String, platform: String) =
        ValorationData(Factory().aPublicReview(),userId,platform, Valoration.DISLIKE)
}