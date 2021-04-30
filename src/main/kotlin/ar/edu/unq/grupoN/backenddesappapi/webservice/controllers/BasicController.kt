package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.CinematographicContentDTO
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@CrossOrigin(origins = ["*"])
@EnableAutoConfiguration
class BasicController(val repository: CinematographicContentRepository) {

    @GetMapping("/")
    fun reviews(): RedirectView {
        return RedirectView("/api/review")
    }

    @GetMapping("/api/content")
    fun content(): List<CinematographicContentDTO> {
        return repository.findAll().map { content -> CinematographicContentDTO.fromModel(content) }
    }

}