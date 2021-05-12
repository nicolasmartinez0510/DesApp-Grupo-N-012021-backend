package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
@Configuration
interface PlatformAdministratorRepository : CrudRepository<PlatformAdministrator, Long?> {
    fun existsByUsernameAndPassword(username: String, password: String): Boolean
}