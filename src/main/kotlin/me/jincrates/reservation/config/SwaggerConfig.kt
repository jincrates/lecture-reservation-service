package me.jincrates.reservation.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket



@Configuration
class SwaggerConfig : WebMvcConfigurer {
    //참고
    //https://springdoc.org/#demos

    //http://localhost:8080/swagger-ui/
    //http://localhost:8080/swagger-ui/index.html
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    fun apiInfo(): ApiInfo = ApiInfoBuilder()
        .title("강연 예약 서비스 RESTful API 명세서")
        .version("1.0.0")
        .description("키다리스튜디오 백엔드팀 과제전형입니다. (이진규)")
        .build()

    @Bean
    fun docket() = Docket(DocumentationType.OAS_30)
        .groupName("전체")
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("me.jincrates.reservation"))
        .paths(PathSelectors.any())
        .build()
        .useDefaultResponseMessages(false)
}