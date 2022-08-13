package me.jincrates.reservation.config

import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiParam
import io.swagger.v3.oas.annotations.media.Schema
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

        //TODO: 이후 인증서버 구축시 변경해야함

        return AuthUser(
            authId = "94042",
            //username = "사용자"
        )
    }
}

data class AuthUser(
    @ApiModelProperty(value = "인증키(사용자 사번)", required = true)
    val authId: String,      //사원번호
    //val username: String,  //사원명
)