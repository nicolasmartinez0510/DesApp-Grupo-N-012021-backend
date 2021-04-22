package ar.edu.unq.grupoN.backenddesappapi.model

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CastMember

data class BasicInformation(val titleId: String, val title: String, val titleType: String,
                            val isAdultContent: Boolean, val startYear: Int, val runtimeMinutes: Int)

data class Cast(val writers: List<CastMember>, val directors: List<CastMember>, val actors: List<CastMember>)

data class RatingInfo(val averageRating: Double, val votesAmount: Int)