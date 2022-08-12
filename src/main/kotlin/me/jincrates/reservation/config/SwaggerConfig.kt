package me.jincrates.reservation.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
class SwaggerConfig : WebMvcConfigurer {

    //http://localhost:8080/swagger-ui/
    //http://localhost:8080/swagger-ui/index.html
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    @Bean
    fun docket(): Docket {
        return Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("me.jincrates.reservation"))
            .paths(PathSelectors.any())
            .build()
    }

}