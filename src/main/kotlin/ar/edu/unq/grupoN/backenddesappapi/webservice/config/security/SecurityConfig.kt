package ar.edu.unq.grupoN.backenddesappapi.webservice.config.security

import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var adminPlatformAdminService: PlatformAdminService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/api/user/me").authenticated()
            .antMatchers("/api/user/subscribe").authenticated()
            .antMatchers("/api/user/**").permitAll()
            .anyRequest().permitAll()
            .and()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
            .addFilterAfter(JWTAuthFilter(adminPlatformAdminService), UsernamePasswordAuthenticationFilter::class.java)
    }


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000",
                                              "https://pacific-shelf-04003.herokuapp.com",
                                              "https://agile-fortress-77406.herokuapp.com")
        configuration.allowedMethods = listOf("GET", "POST")
        configuration.allowCredentials = true
        configuration.exposedHeaders = listOf("*")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}


