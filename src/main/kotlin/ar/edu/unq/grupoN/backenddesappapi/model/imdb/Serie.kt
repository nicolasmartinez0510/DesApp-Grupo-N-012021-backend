package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import javax.persistence.*

@Entity
@PrimaryKeyJoinColumn(name="titleId")
class Serie(
    titleId: String,
    title: String,
    types: List<String>,
    titleType: String,
    isAdult: Boolean,
    startYear: Int,
    runtimeMinutes: Int,
    genres: List<String>,
    directors: List<CastMember>,
    writers: List<CastMember>,
    actors: List<CastMember>,
    averageRating: Double,
    numVotes: Int,
    val endYear: Int?,
    @OneToMany(cascade = [CascadeType.ALL] ,fetch = FetchType.EAGER)
    val seasons: List<Season>,
) :  CinematographicContent(titleId, title, titleType,isAdult, types, startYear, runtimeMinutes,
    genres, directors, writers, actors, averageRating, numVotes){

    override fun isSerie() = true
}