package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.CinematographicContentDTO
import org.springframework.web.bind.annotation.GetMapping

@ServiceREST
class CinematographicController(val repository: CinematographicContentRepository, val reviewRepository: ReviewRepository) {

    @GetMapping("/content")
    fun content(): List<CinematographicContentDTO> {
        return repository.findAll().map { content -> CinematographicContentDTO.fromModel(content) }
    }

    @GetMapping("/reviews")
    fun reviews(): List<Review> {
        return reviewRepository.findAll().toList()
    }

}