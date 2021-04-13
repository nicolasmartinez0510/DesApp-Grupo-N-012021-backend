package ar.edu.unq.grupoN.backenddesappapi.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChanguitoTest {

    @Test
    fun `changuito can add products`(){
        val changuito = Changuito()

        changuito.add("Banana")
        changuito.add("Banana")

        assertThat(changuito.ammountOf("Banana")).isEqualTo(2)
    }
}