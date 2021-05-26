package ar.edu.unq.grupoN.backenddesappapi.model

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.persistence.*

@Entity
class PlatformAdministrator(@Column(unique = true) val email: String,
                            @Column(unique = true) val username: String,
                            @Column(unique = true) val platform: Platform, password:String) {
    val password: String = encript(password)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}


fun encript(password: String): String {
    val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
    val hash: ByteArray = digest.digest(password.toByteArray(StandardCharsets.UTF_8))

    return Base64.getEncoder().encodeToString(hash)
}