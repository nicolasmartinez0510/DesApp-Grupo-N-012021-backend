package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.ReviewType

data class ApplicableFilters(
    val platform: String? = null,
    val includeSpoiler: Boolean? = null,
    val type: String? = null,
    val language: String? = null,
    val geographicLocation: String? = null,
    val contentType: ReviewType? = null,
    val seasonNumber: Int? = null,
    val episodeNumber: Int? = null,
    val orderByDate: Boolean = true,
    val orderByRating: Boolean = true,
    val order: String? = "DESC",
    val page: Int = 0
)