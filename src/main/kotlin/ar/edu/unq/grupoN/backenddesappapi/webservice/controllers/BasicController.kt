package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.ReviewRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.CinematographicContentDTO
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@CrossOrigin(origins = ["*"])
class BasicController {

    @GetMapping("/")
    fun reviews(): RedirectView {
        return RedirectView("/api/content")
    }

}


@ServiceREST
class Entry(val repository: CinematographicContentRepository, val reviewRepository: ReviewRepository) {

    @GetMapping("/content")
    fun content(): List<CinematographicContentDTO> {
        return repository.findAll().map { content -> CinematographicContentDTO.fromModel(content) }
    }

    @GetMapping("/review")
    fun review(): List<ReviewDTO> {
        return reviewRepository.findAll().map { review -> ReviewDTO.fromModel(review) }
    }
}