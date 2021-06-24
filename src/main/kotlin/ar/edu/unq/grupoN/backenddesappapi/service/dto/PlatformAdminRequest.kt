package ar.edu.unq.grupoN.backenddesappapi.service.dto

import ar.edu.unq.grupoN.backenddesappapi.model.Platform
import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.URL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.*

@ApiModel(description = "Requested information from a new administrator of a specific platform. Only one per platform.")
data class RegisterRequest(
    @ApiModelProperty(value = "username", example = "exampleuser", required = true)
    @field:NotBlank
    @field:Pattern(regexp= "^[a-zA-Z0-9]*$")
    val username: String,
    @ApiModelProperty(value = "platform", example = "AMAZON", required = true)
    val platform: Platform,
    @ApiModelProperty(value = "email", example = "example@example.com", required = true)
    @field:NotBlank
    @field:Email
    @field:Pattern(regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}\$")
    val email: String,
    @ApiModelProperty(value = "password", example = "1234", required = true)
    @field:NotBlank
    @field:Min(value = 4)
    @field:Max(value = 16)
    val password: String,
) {
    fun toModel() = PlatformAdministrator(email, username, platform, password)
}

@ApiModel(description = "Requested information to log in re-senia api.")
data class LoginCredentialsRequest(
    @ApiModelProperty(value = "username", example = "chester", required = true)
    @field:NotBlank
    val username: String,
    @ApiModelProperty(value = "password", example = "1234", required = true)
    @field:NotBlank
    val password: String
)

@ApiModel(description = "Represents a user platform information.")
class PlatformAdminInfo(
    val apiKey: String,
    val username: String,
    val email: String,
    val platform: Platform,
    val addReview: Int,
    val report: Int,
    val rate: Int,
    val search: Int,
    val contentSearch: Int
) {
    companion object {
        fun fromModel(platformAdminModel: PlatformAdministrator): PlatformAdminInfo {
            return PlatformAdminInfo(
                platformAdminModel.uuid!!,
                platformAdminModel.username,
                platformAdminModel.email,
                platformAdminModel.platform,
                platformAdminModel.addReview,
                platformAdminModel.report,
                platformAdminModel.rate,
                platformAdminModel.search,
                platformAdminModel.contentSearch
            )
        }
    }
}

@Entity
class SubscribedUrl(
    @field:NotBlank
    val apiKey:String,
    @field:NotBlank
    @field:URL
    @field:Pattern(regexp = "(http(s)?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    val url: String) {
    @Id
    @GeneratedValue
    var id: Long? = null
}
