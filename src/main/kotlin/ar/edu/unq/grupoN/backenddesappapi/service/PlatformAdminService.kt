package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.AlreadyExistException
import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import ar.edu.unq.grupoN.backenddesappapi.model.encript
import ar.edu.unq.grupoN.backenddesappapi.persistence.PlatformAdministratorRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.AdminCredentials
import ar.edu.unq.grupoN.backenddesappapi.service.dto.AdminPlatformInfo
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlatformAdminService {

    @Autowired
    private lateinit var repository: PlatformAdministratorRepository

    @Transactional
    fun register(adminPlatformInfo: AdminPlatformInfo): AdminPlatformInfo {
        val savedAdmin: PlatformAdministrator

        if(EmailValidator.getInstance().isValid(adminPlatformInfo.email)) {
            try{
                savedAdmin = repository.save(adminPlatformInfo.toModel())
            } catch (e: DataIntegrityViolationException) {
                throw AlreadyExistException("Email, platform or username already registered.")
            }
        } else {
            throw RuntimeException("Invalid email.")
        }

        return AdminPlatformInfo(savedAdmin.id, savedAdmin.username, savedAdmin.platform, savedAdmin.email, savedAdmin.password)
    }

    fun login(adminCredentials: AdminCredentials): String {
        val encryptedPassword = encript(adminCredentials.password)
        return if (repository.existsByUsernameAndPassword(adminCredentials.username, encryptedPassword)){
            "Login succesfull"
        } else {
            throw RuntimeException("Invalid credentials.")
        }
    }
}