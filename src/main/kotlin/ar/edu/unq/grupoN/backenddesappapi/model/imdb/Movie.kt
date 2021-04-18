package ar.edu.unq.grupoN.backenddesappapi.model.imdb

class Movie(
    titleId: String,
    ordering: Int,
    title: String,
    region: String,
    language: String,
    types: List<String>,
    attributes: List<String>,
    isOriginalTitle: Boolean,
    titleType: String,
    primaryTitle: String,
    originalTitle: String,
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
    CinematographicContent(titleId, ordering, title, region, language, types, attributes,
    isOriginalTitle, titleType, primaryTitle, originalTitle, isAdult, startYear, runtimeMinutes,
    genres, directors, writers, actors, averageRating, numVotes)
