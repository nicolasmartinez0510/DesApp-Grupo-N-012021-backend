package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.Employment
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.Serie

abstract class CinematographicContentDTO {
    companion object {
        fun fromModel(content: CinematographicContent): CinematographicContentDTO {
            val actors = takeFrom(content, Employment.ACTOR)
            val directors = takeFrom(content, Employment.DIRECTOR)
            val writers = takeFrom(content, Employment.WRITER)

            return if (content.isSerie()) {
                val serie = content as Serie
                SerieDTO(
                    serie.titleId,
                    serie.title,
                    serie.startYear!!,
                    actors,
                    directors,
                    writers,
                    seasons = serie.seasons.size
                )
            } else {
                MovieDTO(content.titleId, content.title, content.startYear!!, actors, directors, writers)
            }
        }

        private fun takeFrom(content: CinematographicContent, anEmployment: Employment): List<String> {
            return content.cast.filter { it.employment == anEmployment }.map { it.name }
        }
    }

    open fun isMovie() = false
}

class MovieDTO(
    val titleId: String, val title: String,
    val startYear: Int,
    val actorsName: List<String>,
    val directorsName: List<String>,
    val writersName: List<String>,
    var rating: Double = 0.0,
    var votesAmount: Long = 0
) : CinematographicContentDTO() {
    override fun isMovie() = true
}

class SerieDTO(
    val titleId: String, val title: String,
    val startYear: Int,
    val actorsName: List<String>,
    val directorsName: List<String>,
    val writersName: List<String>,
    var rating: Double = 0.0,
    var votesAmount: Long = 0,
    val seasons: Int
) : CinematographicContentDTO()