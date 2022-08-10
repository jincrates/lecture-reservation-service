package me.jincrates.lecturereservationservice.domain.annotation

import me.jincrates.lecturereservationservice.domain.validator.StringFormatDateTimeValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

//TODO: 여기 어노테이션 뭔지 확인하기
@Constraint(validatedBy = [StringFormatDateTimeValidator::class])
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class StringFormatDateTime(
    val pattern: String = "yyyy-MM-dd HH:mm:ss",
    val message: String = "시간 형식이 유효하지 않습니다.",
    //아래는 어노테이션 default 설정
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
