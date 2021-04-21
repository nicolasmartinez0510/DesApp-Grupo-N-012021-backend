package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Episode(val number:Int) {
    @Id
    @GeneratedValue
    var id: Long? = null
}
