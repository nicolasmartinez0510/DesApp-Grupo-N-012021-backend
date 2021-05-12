package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.Platform
import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Requested information from a new administrator of a specific platform. Only one per platform.")
data class AdminPlatformInfo(
    @ApiModelProperty(hidden = true)
    val id: Long?,
    val username: String,
    val platform: Platform,
    val email: String,
    val password: String,
) {
    fun toModel() = PlatformAdministrator(email, username, platform, password)
}

data class AdminCredentials(val username: String, val password: String)