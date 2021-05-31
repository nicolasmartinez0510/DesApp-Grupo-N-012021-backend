package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.ServiceREST
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*


@Configuration
@EnableSwagger2
class SwaggerConfig {
    
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(apiKey(), bearerToken()))
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(ServiceREST::class.java))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(metaInfo())
    }

    private fun apiKey(): ApiKey {
        return ApiKey("Api key", "Authorization", "header")
    }

    private fun bearerToken(): ApiKey {
        return ApiKey("JWT", "Authentication", "header")
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder().securityReferences(defaultAuth()).build()
    }

    private fun defaultAuth(): List<SecurityReference?> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls(1)
        authorizationScopes[0] = authorizationScope
        return listOf(
            SecurityReference("Api key", authorizationScopes),
            SecurityReference("JWT", authorizationScopes)
        )
    }

    private fun metaInfo(): ApiInfo {
        return ApiInfo(
            "Re-Senia API REST",
            "Made in a 'ideal world' where all streamings platforms uses same API REST for manage reviews from his clients over his contents.",
            "1.0",
            "https://www.youtube.com/watch?v=a1i3KkGVF8c",
            Contact(
                "N-Team", "https://github.com/nicolasmartinez0510/DesApp-Grupo-N-012021-backend",
                "federico.sandoval@alu.unq.edu.ar"
            ),
            "Apache License Version 2.0",
            "https://www.apache.org/licenses/LICENSE-2.0", ArrayList()
        )
    }
}