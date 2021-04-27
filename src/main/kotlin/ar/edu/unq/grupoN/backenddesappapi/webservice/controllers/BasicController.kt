package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.CinematographicContentDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import org.springframework.web.bind.annotation.GetMapping

@ServiceREST
class BasicController(val repository: CinematographicContentRepository, val reviewRepository: ReviewRepository) {

    @GetMapping("/content")
    fun content(): List<CinematographicContentDTO> {
        return repository.findAll().map { content -> CinematographicContentDTO.fromModel(content) }
    }

    @GetMapping("/review")
    fun reviews(): List<ReviewDTO> {
        return reviewRepository.findAll().map{ review -> ReviewDTO.fromModel(review) }
    }

}