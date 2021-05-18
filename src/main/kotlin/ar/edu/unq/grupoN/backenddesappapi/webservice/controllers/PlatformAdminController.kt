package ar.edu.unq.grupoN.backenddesappapi.webservice.controllers

import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import ar.edu.unq.grupoN.backenddesappapi.service.dto.AdminCredentials
import ar.edu.unq.grupoN.backenddesappapi.service.dto.AdminPlatformInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.lang.RuntimeException

@ServiceREST
@RequestMapping("/api/user")
@EnableAutoConfiguration
class PlatformAdminController {
    @Autowired
    private lateinit var platformAdminService: PlatformAdminService

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun register(@RequestBody adminPlatformInfo: AdminPlatformInfo): ResponseEntity<*>?{
        return try {
            ResponseEntity.ok(platformAdminService.register(adminPlatformInfo))
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().body(createExceptionResponse(e))
        }
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@RequestBody adminCredentials: AdminCredentials): ResponseEntity<*>?{
        return try {
            ResponseEntity.ok(platformAdminService.login(adminCredentials))
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().body(createExceptionResponse(e))
        }
    }
}