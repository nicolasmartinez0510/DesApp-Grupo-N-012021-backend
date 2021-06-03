package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.LoginCredentialsRequest
import ar.edu.unq.grupoN.backenddesappapi.service.dto.RegisterRequest
import io.jsonwebtoken.Jwts
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@ServiceREST
@RequestMapping("/api/user")
@EnableAutoConfiguration
class PlatformAdminController {
    @Autowired
    private lateinit var platformAdminService: PlatformAdminService
    @ApiOperation(
        value = "For register in system and get a api key.This operation is available for all users."
    )
    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*>? {
        val responseHeaders = HttpHeaders()
        responseHeaders.set("Authorization", platformAdminService.register(registerRequest))

        return ResponseEntity.status(HttpStatus.CREATED)
            .headers(responseHeaders)
            .body("REGISTERED SUCCESSFULLY")
    }
    @ApiOperation(
        value = "For login in system, view and get a you api key.This operation is available for all users."
    )
    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@RequestBody loginCredentialsRequest: LoginCredentialsRequest): ResponseEntity<*>? {
        val accessControlHeaders = platformAdminService.login(loginCredentialsRequest)
        val responseHeaders = HttpHeaders()

        responseHeaders.set("Authorization", accessControlHeaders["Authorization"])
        responseHeaders.set("Authentication", accessControlHeaders["Authentication"])

        return ResponseEntity.status(HttpStatus.OK)
            .headers(responseHeaders)
            .body("LOGGED IN SUCCESSFULLY")
    }
    @ApiOperation(
        value = "For see info of you user in system.This operation is available only for authenticated users"
    )
    @RequestMapping(value=["/me"], method = [RequestMethod.GET])
    fun userInfo(@RequestHeader("Authentication") authHeader: String): ResponseEntity<*>? {
        return ResponseEntity.ok(platformAdminService.findByToken(authHeader))
    }


}