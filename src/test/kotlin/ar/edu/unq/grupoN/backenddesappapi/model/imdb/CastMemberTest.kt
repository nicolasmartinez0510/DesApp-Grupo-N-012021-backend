package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CastMemberTest {

    val norman = CastMember("Maaya Uchida", "Actor",
        "Voice Actor", "Norman", 9999, null, listOf("Voice Actor"))

    @Test
    fun `a cast member know IMDb information`(){

        assertThat(norman.name).isEqualTo("Maaya Uchida")
        assertThat(norman.category).isEqualTo("Actor")
        assertThat(norman.job).isEqualTo("Voice Actor")
        assertThat(norman.character).isEqualTo("Norman")
        assertThat(norman.birthYear).isEqualTo(9999)
        assertThat(norman.deathYear).isNull()
        assertThat(norman.character).isEqualTo("Norman")
        assertThat(norman.primaryProfession).containsExactly("Voice Actor")
    }

}