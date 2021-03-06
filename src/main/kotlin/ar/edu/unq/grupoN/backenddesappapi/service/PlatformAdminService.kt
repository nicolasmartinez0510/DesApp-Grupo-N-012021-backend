package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.Metric
import ar.edu.unq.grupoN.backenddesappapi.model.Platform
import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import ar.edu.unq.grupoN.backenddesappapi.model.encript
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.PlatformAdministratorRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.LoginCredentialsRequest
import ar.edu.unq.grupoN.backenddesappapi.service.dto.PlatformAdminInfo
import ar.edu.unq.grupoN.backenddesappapi.service.dto.RegisterRequest
import ar.edu.unq.grupoN.backenddesappapi.service.dto.SubscribedUrl
import ar.edu.unq.grupoN.backenddesappapi.service.exceptions.AlreadyExistException
import ar.edu.unq.grupoN.backenddesappapi.service.exceptions.InvalidCredentialsException
import ar.edu.unq.grupoN.backenddesappapi.service.exceptions.InvalidEmailException
import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.SubscribeContentRequest
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors


@Service
class PlatformAdminService {

    @Autowired
    private lateinit var repository: PlatformAdministratorRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Transactional
    fun register(registerRequest: RegisterRequest): String {
        val savedAdmin: PlatformAdministrator

        if (EmailValidator.getInstance().isValid(registerRequest.email)) {
            try {
                val newAdminPlatform = registerRequest.toModel()
                newAdminPlatform.uuid = UUID.randomUUID().toString().toUpperCase()

                savedAdmin = repository.save(newAdminPlatform)
            } catch (e: DataIntegrityViolationException) {
                throw AlreadyExistException()
            }
        } else {
            throw InvalidEmailException()
        }

        return savedAdmin.uuid!!
    }

    @Transactional
    fun login(loginCredentialsRequest: LoginCredentialsRequest): Map<String, String> {
        val encryptedPassword = encript(loginCredentialsRequest.password)
        val adminPlatform = repository.findByUsernameAndPassword(loginCredentialsRequest.username, encryptedPassword)
        return if (adminPlatform != null) {
            val authentication = Pair(
                "Authentication",
                generateToken(adminPlatform.id!!, adminPlatform.username, adminPlatform.platform.toString())
            )
            val authorization = Pair("Authorization", "Api key ${adminPlatform.uuid!!}")

            mapOf(authentication, authorization)
        } else {
            throw InvalidCredentialsException()
        }
    }

    @Transactional
    fun activeNotificationsTo(apiKey: String, subscribeContentRequest: SubscribeContentRequest): String {
        val content = contentRepository.findById(subscribeContentRequest.titleId).get()

        content.addToSubscribers(SubscribedUrl(apiKey, subscribeContentRequest.url))
        contentRepository.save(content)

        return "Subscribed to ${content.titleId} succesfully with url ${subscribeContentRequest.url}"
    }


    @Transactional
    fun findByToken(authHeader: String): PlatformAdminInfo {
        val userId =
            Jwts
                .parser()
                .setSigningKey("reseniaNGroupSecretKey".toByteArray())
                .parseClaimsJws(authHeader.replace("Bearer ", ""))
                .body
                .id
                .toLong()

        return PlatformAdminInfo.fromModel(repository.findById(userId).get())
    }

    @Transactional
    fun existByApiKey(apiKey: String) = repository.existsByUuid(apiKey)

    @Transactional
    fun existsByUsernameAndPlatform(username: String, platform: Platform) =
        repository.existsByUsernameAndPlatform(username, platform)

    @Transactional
    fun addMetric(apiKey: String,metric: Metric){
        val user: PlatformAdministrator = repository.findByUuid(apiKey)
        user.addMetric(metric)
        repository.save(user)
    }

    private fun generateToken(id: Long, username: String, platform: String): String {
        val secretKey = "reseniaNGroupSecretKey"
        val grantedAuthorities = AuthorityUtils
            .commaSeparatedStringToAuthorityList("ROLE_USER")
        val token: String = Jwts
            .builder()
            .setId(id.toString())
            .setSubject("Authenticating user with JWT")
            .claim("username", username)
            .claim("platform", platform)
            .claim("authorities",
                grantedAuthorities.stream()
                    .map { obj: GrantedAuthority -> obj.authority }
                    .collect(Collectors.toList()))
            .setIssuedAt(Date(System.currentTimeMillis()))
            //set authentication expiration time in 24 hs.
            .setExpiration(Date(System.currentTimeMillis().plus(1000 * 60 * 60 * 24)))
            .signWith(
                SignatureAlgorithm.HS512,
                secretKey.toByteArray()
            ).compact()

        return "Bearer $token"
    }
}