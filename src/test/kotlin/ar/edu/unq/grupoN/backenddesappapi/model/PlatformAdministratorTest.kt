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
    }
}