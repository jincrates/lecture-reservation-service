package me.jincrates.lecturereservationservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

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

        return AuthUser(
            userId = 1,
            userCode = "94042",
            username = "사용자"
        )
    }

}

data class AuthUser(
    val userId: Long,
    val userCode: String,  //사원번호
    val username: String,  //사원명
)