package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ApplicableFilters

interface ReviewRepositoryCustom {

    fun findReviews(content: CinematographicContent, applicableFilters: ApplicableFilters): List<Review>

    fun findContentInReverseSearch() : List<CinematographicContent>
}