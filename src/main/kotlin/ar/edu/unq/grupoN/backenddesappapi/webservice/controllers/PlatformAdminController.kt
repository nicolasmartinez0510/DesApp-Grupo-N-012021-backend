package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.aspect.Authorize
import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.LoginCredentialsRequest
import ar.edu.unq.grupoN.backenddesappapi.service.dto.RegisterRequest
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.hibernate.validator.constraints.URL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

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
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<*>? {
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
    fun login(@Valid @RequestBody loginCredentialsRequest: LoginCredentialsRequest): ResponseEntity<*>? {
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
    fun userInfo(
        @ApiParam(hidden = true)
        @RequestHeader("Authentication")
        apiKey: String
    ): ResponseEntity<*>? {
        return ResponseEntity.ok(platformAdminService.findByToken(apiKey))
    }

    @ApiOperation(
        value = "Subscribe to receive notifications on new reviews from a content. Authorization and Authentication required."
    )
    @Authorize
    @RequestMapping(value = ["/subscribe"], method = [RequestMethod.POST])
    fun subscribeToReceiveNotificationsOn(
        @Valid @RequestBody
        subscribeContentRequest: SubscribeContentRequest,
        @ApiParam(hidden = true)
        @RequestHeader("Authorization")
        apiKey: String): ResponseEntity<*>?{

        val response = platformAdminService.activeNotificationsTo(apiKey, subscribeContentRequest)

        return ResponseEntity.ok(response)
    }
}


data class SubscribeContentRequest(
    @ApiModelProperty(value= "desired content titleId", example = "GladiatorID")
    @field:NotBlank
    val titleId: String,
    @ApiModelProperty(value="url for notify news reviews over the content", example="https://rickandmortyapi.com/api/character/2")
    @field:NotBlank
    @field:URL
    @field:Pattern(regexp="(http(s)?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    val url: String
    )