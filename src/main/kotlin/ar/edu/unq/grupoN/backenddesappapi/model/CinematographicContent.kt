package ar.edu.unq.grupoN.backenddesappapi.model

open class CinematographicContent(
    val titleId: String,
    val ordering: Int,
    val title: String,
    val region: String,
    val language: String,
    val types: List<String>,
    val attributes: List<String>,
    val isOriginalTitle: Boolean,
    val titleType: String,
    val primaryTitle: String,
    val originalTitle: String,
    val isAdult: Boolean,
    val startYear: Int,
    val runtimeMinutes: Int,
    val genres: List<String>,
    val directors: List<CastMember>,
    val writers: List<CastMember>,
    val actors: List<CastMember>,
    val averageRating: Double,
    val numVotes: Int
)
