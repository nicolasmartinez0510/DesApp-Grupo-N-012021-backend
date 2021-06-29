package ar.edu.unq.grupoN.backenddesappapi.model

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.persistence.*

@Entity
class PlatformAdministrator(
    @Column(unique = true) val email: String,
    @Column(unique = true) val username: String,
    @Column(unique = true) val platform: Platform,
    password: String
) {
    val password: String = encript(password)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var uuid: String? = null
    var addReview: Int = 0
    var report: Int = 0
    var rate: Int = 0
    var search: Int = 0
    var contentSearch: Int = 0


    fun addMetric(metric: Metric){
        when(metric){
            Metric.ADDREVIEW -> addReview += 1
            Metric.REPORT -> report += 1
            Metric.SEARCH -> search += 1
            Metric.RATE -> rate += 1
            Metric.CONTENTSEARCH -> contentSearch += 1
        }
    }
}


fun encript(password: String): String {
    val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
    val hash: ByteArray = digest.digest(password.toByteArray(StandardCharsets.UTF_8))

    return Base64.getEncoder().encodeToString(hash)
}
