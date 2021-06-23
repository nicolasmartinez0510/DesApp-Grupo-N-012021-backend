package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ApplicableFilters
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PerformedContent
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReverseSearchFilter

interface ReviewRepositoryCustom {

    fun findReviews(content: CinematographicContent, applicableFilters: ApplicableFilters): List<Review>

    fun findContentInReverseSearch(reverseSearchFilter: ReverseSearchFilter) : List<CinematographicContent>

    fun contentBasicInfo(titleId: String): PerformedContent

    fun allContentsBasicInfo(): MutableList<PerformedContent>
}