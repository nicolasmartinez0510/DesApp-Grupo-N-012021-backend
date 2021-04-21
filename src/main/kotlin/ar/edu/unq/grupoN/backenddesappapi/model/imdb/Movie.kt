package ar.edu.unq.grupoN.backenddesappapi.model.imdb

import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="titleId")
class Movie(
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
    numVotes: Int
) :
    CinematographicContent(titleId, title, titleType,isAdult, types, startYear, runtimeMinutes,
    genres, directors, writers, actors, averageRating, numVotes)
