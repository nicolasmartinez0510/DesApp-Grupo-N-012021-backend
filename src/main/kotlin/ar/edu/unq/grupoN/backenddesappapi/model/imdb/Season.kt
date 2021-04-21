package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import javax.persistence.*

@Entity
data class Season(val number:Int,
                  @OneToMany(cascade = [CascadeType.ALL])
                  val episodes: List<Episode>) {
    @Id
    var id: Long? = null
}