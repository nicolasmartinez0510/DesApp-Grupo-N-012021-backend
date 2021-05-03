package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import javax.persistence.*

@Entity
data class Season(val number:Int,
                  @OneToMany(cascade = [CascadeType.ALL])
                  val episodes: List<Episode>) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun isSeason(seasonNumber: Int?) = seasonNumber == this.number

    fun haveEpisode(episodeNumber: Int?) = episodes.any { it.number == episodeNumber }
}