package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.model.Employment
import javax.persistence.*

@Entity
data class CastMember(val name: String, val employment: Employment, val job: String?, val character: String,
                      val birthYear: Int, val deathYear: Int?){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
}