package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import javax.persistence.*

@Entity
data class CastMember(val name: String, val employment: String, val job: String?, val character: String,
                      val birthYear: Int, val deathYear: Int?) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}