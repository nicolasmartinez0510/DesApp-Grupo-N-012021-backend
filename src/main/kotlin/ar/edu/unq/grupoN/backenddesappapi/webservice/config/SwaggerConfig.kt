package ar.edu.unq.grupoN.backenddesappapi.webservice.config

import ar.edu.unq.grupoN.backenddesappapi.webservice.controllers.ServiceREST
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(ServiceREST::class.java))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(metaInfo())
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