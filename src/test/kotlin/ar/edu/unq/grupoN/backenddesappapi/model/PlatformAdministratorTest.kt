package ar.edu.unq.grupoN.backenddesappapi.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlatformAdministratorTest {

    @Test
    fun `a platform administrator knows his fields where his password is encrypted`() {
        val user = PlatformAdministrator("fede@gmail.com", "gabo", Platform.NETFLIX, "hola123")
        val encryptedExpectedPasword = encript("hola123")

        assertThat(user.email).isEqualTo("fede@gmail.com")
        assertThat(user.username).isEqualTo("gabo")
        assertThat(user.platform).isEqualTo(Platform.NETFLIX)
        assertThat(user.password).isEqualTo(encryptedExpectedPasword)
        assertThat(user.id).isNull()
        assertThat(user.uuid).isNull()
    }

    @Test
    fun `a platform administrator knows your metrics`() {
        val user = PlatformAdministrator("fede@gmail.com", "gabo", Platform.NETFLIX, "hola123")

        user.addMetric(Metric.ADDREVIEW)
        user.addMetric(Metric.RATE)
        user.addMetric(Metric.REPORT)
        user.addMetric(Metric.SEARCH)
        user.addMetric(Metric.CONTENTSEARCH)

        assertThat(user.addReview).isEqualTo(1)
        assertThat(user.rate).isEqualTo(1)
        assertThat(user.contentSearch).isEqualTo(1)
        assertThat(user.search).isEqualTo(1)
        assertThat(user.report).isEqualTo(1)
    }

}