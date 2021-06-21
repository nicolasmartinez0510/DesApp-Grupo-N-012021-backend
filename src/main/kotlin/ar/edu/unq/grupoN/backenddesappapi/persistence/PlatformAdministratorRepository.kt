package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.Platform
import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
@Configuration
interface PlatformAdministratorRepository : CrudRepository<PlatformAdministrator, Long?> {

    fun existsByUuid(uuid: String): Boolean

    fun existsByUsernameAndPlatform(username: String, platform: Platform): Boolean

    fun findByUsernameAndPassword(username: String, encryptedPassword: String): PlatformAdministrator?

    fun findByUuid(uuid: String): PlatformAdministrator
}
