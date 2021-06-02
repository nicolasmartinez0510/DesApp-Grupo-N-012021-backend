package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.Platform
import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Requested information from a new administrator of a specific platform. Only one per platform.")
data class RegisterRequest(
    @ApiModelProperty(value = "username", example = "exampleuser", required = true)
    val username: String,
    @ApiModelProperty(value = "platform", example = "AMAZON", required = true)
    val platform: Platform,
    @ApiModelProperty(value = "email", example = "example@example.com", required = true)
    val email: String,
    @ApiModelProperty(value = "password", example = "1234", required = true)
    val password: String,
) {
    fun toModel() = PlatformAdministrator(email, username, platform, password)
}

@ApiModel(description = "Requested information to log in re-senia api.")
data class LoginCredentialsRequest(
    @ApiModelProperty(value = "username", example = "chester", required = true)
    val username: String,
    @ApiModelProperty(value = "password", example = "1234", required = true)
    val password: String
)

@ApiModel(description = "Represents a user platform information.")
class PlatformAdminInfo(val id: Long, val apiKey: String, val email: String, val username: String) {
    companion object {
        fun fromModel(platformAdminModel: PlatformAdministrator): PlatformAdminInfo {
            return PlatformAdminInfo(
                platformAdminModel.id!!,
                platformAdminModel.uuid!!,
                platformAdminModel.email,
                platformAdminModel.username
            )
        }
    }
}