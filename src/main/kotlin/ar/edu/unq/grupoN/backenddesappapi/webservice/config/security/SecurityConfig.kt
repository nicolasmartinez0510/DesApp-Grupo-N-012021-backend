package ar.edu.unq.grupoN.backenddesappapi.webservice.config.security

import ar.edu.unq.grupoN.backenddesappapi.service.PlatformAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var adminPlatformAdminService: PlatformAdminService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/api/user/me").authenticated()
            .antMatchers("/api/user/**").permitAll()
            .anyRequest().permitAll()
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
            .addFilterAfter(JWTAuthFilter(adminPlatformAdminService), UsernamePasswordAuthenticationFilter::class.java)
        http.headers().frameOptions().disable()
    }
}