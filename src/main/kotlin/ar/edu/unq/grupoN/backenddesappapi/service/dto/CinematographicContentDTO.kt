package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CastMember
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.Season
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.Serie
import ar.edu.unq.grupoN.backenddesappapi.model.Employment

abstract class CinematographicContentDTO{
    companion object {
        fun fromModel(content: CinematographicContent): CinematographicContentDTO {
            val actors = takeFrom(content, Employment.ACTOR)
            val directors = takeFrom(content, Employment.DIRECTOR)
            val writers = takeFrom(content, Employment.WRITER)

            return if (content.isSerie()){
                val serie = content as Serie
                SerieDTO(serie.titleId, serie.title, actors, directors, writers, serie.averageRating, serie.seasons)
            } else {
                MovieDTO(content.titleId, content.title, actors, directors, writers, content.averageRating)
            }
        }

        private fun takeFrom(content: CinematographicContent, anEmployment: Employment): MutableList<CastMember> {
            return content.cast.filter { it.employment == anEmployment }.toMutableList()
        }
    }
}

class MovieDTO(val titleId: String, val title: String,
               val actors: List<CastMember>,
               val directors: List<CastMember>,
               val writers: List<CastMember>,
               val rating: Double,) : CinematographicContentDTO()

class SerieDTO(val titleId: String, val title: String,
               val actors: List<CastMember>,
               val directors: List<CastMember>,
               val writers: List<CastMember>,
               val rating: Double,
               val seasons: List<Season>) : CinematographicContentDTO()