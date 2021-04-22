package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import ar.edu.unq.grupoN.backenddesappapi.model.BasicInformation
import ar.edu.unq.grupoN.backenddesappapi.model.Cast
import ar.edu.unq.grupoN.backenddesappapi.model.RatingInfo
import javax.persistence.*

@Entity
@PrimaryKeyJoinColumn(name="titleId")
class Serie(
    basicInformation: BasicInformation,
    cast: Cast,
    rating: RatingInfo,
    val endYear: Int?,
    @OneToMany(cascade = [CascadeType.ALL] ,fetch = FetchType.EAGER)
    val seasons: List<Season>,
) :  CinematographicContent(basicInformation, cast, rating){

    override fun isSerie() = true
}