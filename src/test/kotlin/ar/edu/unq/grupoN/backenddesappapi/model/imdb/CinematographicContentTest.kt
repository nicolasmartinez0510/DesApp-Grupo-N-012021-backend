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
        spartacus = factory.spartacus_serie()
        gladiator = factory.gladiator_movie()
    }

    @Test
    fun `a cinematographic content know all IMDb information`(){
        assertThat(gladiator).usingRecursiveComparison()
            .isEqualTo(factory.gladiator_movie())
    }

    @Test
    fun `series have extra information about IMDb information`(){
        assertThat(spartacus.endYear).isEqualTo(2013)
        assertThat(spartacus.seasons).usingRecursiveComparison()
            .isEqualTo(listOf(Season(2, listOf(Episode(1)))))
    }
}