package me.jincrates.reservation.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@EnableWebMvc
@Configuration
class WebConfig(
    private val authUserHandlerArgumentsResolver: AuthUserHandlerArgumentsResolver,
) : WebMvcConfigurationSupport() {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.apply {
            add(authUserHandlerArgumentsResolver)
        }
    }
}

@Component
class AuthUserHandlerArgumentsResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        AuthUser::class.java.isAssignableFrom(parameter.parameterType)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {

        //TODO: 이후 인증서버 구축시 변경해야함
        return AuthUser(
            userId = "94042",
            //username = "사용자"
        )
    }
}

data class AuthUser(
    val userId: String,      //사원번호
    //val username: String,  //사원명
)