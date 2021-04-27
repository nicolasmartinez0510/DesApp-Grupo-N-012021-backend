package ar.edu.unq.grupoN.backenddesappapi.model.review

import ar.edu.unq.grupoN.backenddesappapi.Factory
import ar.edu.unq.grupoN.backenddesappapi.model.ValorationData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ValorationDataTest {

    @Test
    fun `a valoration data knew his attributes afters set all`(){
        val anotherReview = Factory().a_public_review()
        val valorationData = ValorationData(anotherReview,"ChestId","Netflix",Valoration.LIKE)
        valorationData.review = Factory().a_public_review()
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

    private fun valorationFrom(userId: String, platform: String) =
        ValorationData(Factory().a_public_review(),userId,platform,Valoration.DISLIKE)
}