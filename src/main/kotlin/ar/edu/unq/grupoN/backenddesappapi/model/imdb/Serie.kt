package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.model.BasicInformation
import ar.edu.unq.grupoN.backenddesappapi.model.RatingInfo
import ar.edu.unq.grupoN.backenddesappapi.model.SerieInfo
import javax.persistence.*

@Entity
@PrimaryKeyJoinColumn(name="titleId")
class Serie(
    basicInformation: BasicInformation,
    cast: MutableList<CastMember>,
    rating: RatingInfo,
    serieInfo: SerieInfo
) :  CinematographicContent(basicInformation, cast, rating){

    var endYear: Int? = null

    @OneToMany(cascade = [CascadeType.ALL] ,fetch = FetchType.EAGER)
    var seasons: List<Season>

    init {
        endYear = serieInfo.endYear
        seasons = serieInfo.seasons
    }

    override fun isSerie() = true

    override fun haveEpisode(seasonNumber: Int?, episodeNumber: Int?): Boolean {
        val searchedSeason = seasons.firstOrNull { it.isSeason(seasonNumber) }

        return searchedSeason?.haveEpisode(episodeNumber) ?: false
    }
}