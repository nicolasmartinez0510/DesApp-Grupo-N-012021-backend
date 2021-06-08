package ar.edu.unq.grupoN.backenddesappapi.webservice.config.security

import ar.edu.unq.grupoN.backenddesappapi.model.Platform
import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JWTAuthFilter(val adminPlatformService: PlatformAdminService) : OncePerRequestFilter() {
    private val HEADER = "Authentication"
    private val PREFIX = "Bearer "
    private val SECRET = "reseniaNGroupSecretKey"

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        validateToken(request)

        try {
            chain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        } catch (e: UnsupportedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        } catch (e: MalformedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        }
    }

    private fun validateToken(request: HttpServletRequest) {
        var claims: Claims? = null
        if (existJWTToken(request)) claims = obtainClaims(request)

        if (
            claims != null &&
            haveValidsClaims(claims) &&
            isValidUserFromPlatform(
                claims["username"] as String,
                toPlatform(claims["platform"].toString())
            )
        ) {
            setUpSpringAuthentication(claims)
        } else {
            SecurityContextHolder.clearContext()
        }
    }

    private fun obtainClaims(request: HttpServletRequest): Claims {
        val jwtToken = request.getHeader(HEADER).replace(PREFIX, "")
        return Jwts.parser().setSigningKey(SECRET.toByteArray()).parseClaimsJws(jwtToken).body
    }

    private fun setUpSpringAuthentication(claims: Claims) {
        val authorities = claims["authorities"] as List<String>?
        val auth = UsernamePasswordAuthenticationToken(
            Pair(claims["username"].toString(), toPlatform(claims["platform"].toString())),
            null,
            authorities!!.stream().map { role: String? ->
                SimpleGrantedAuthority(
                    role
                )
            }.collect(Collectors.toList())
        )
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun existJWTToken(request: HttpServletRequest): Boolean {
        val authenticationHeader = request.getHeader(HEADER)
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX)
    }

    private fun haveValidsClaims(claims: Claims) =
        claims["authorities"] != null && claims["username"] != null && claims["platform"] != null

    private fun isValidUserFromPlatform(username: String, platform: Platform) =
        adminPlatformService.existsByUsernameAndPlatform(username, platform)

    private fun toPlatform(stringPlatform: String) =
        when (stringPlatform.toUpperCase()) {
            "PLEX" -> Platform.PLEX
            "NETFLIX" -> Platform.NETFLIX
            "AMAZON" -> Platform.AMAZON
            else -> {
                Platform.DISNEY
            }
        }
}