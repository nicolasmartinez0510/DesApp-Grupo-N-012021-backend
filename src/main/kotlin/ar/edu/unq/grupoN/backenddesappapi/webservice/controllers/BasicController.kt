package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.CinematographicContentDTO
import io.swagger.annotations.ApiOperation
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@RestController
@CrossOrigin(origins = ["*"])
@EnableAutoConfiguration
class BasicController(val repository: CinematographicContentRepository) {

    @ApiOperation(value = "Endpoint used to show Re-Senia API docs.", hidden = true)
    @RequestMapping(value = ["/",""], method = [RequestMethod.GET])
    fun reviews(): RedirectView {
        return RedirectView("/swagger-ui.html")
    }

    @ApiOperation(value = "Endpoint used for api develop. to show generated fake reviews", hidden = true)
    @RequestMapping(value = ["/api/content"], method = [RequestMethod.GET])
    fun content(): List<CinematographicContentDTO> {
        return repository.findAll().map { content -> CinematographicContentDTO.fromModel(content) }
    }

}