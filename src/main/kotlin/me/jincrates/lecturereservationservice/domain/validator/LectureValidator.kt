package me.jincrates.lecturereservationservice.domain.validator

import me.jincrates.lecturereservationservice.model.LectureRequest
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import java.time.LocalDateTime

@Component
class LectureValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return LectureRequest::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val request: LectureRequest = target as LectureRequest

        if (isNotValidOpenedAt(request)) {
            errors.rejectValue("openedAt", "invalid.createdAt", "강연 시작일시를 정확히 입력하세요.")
        }

        if (isNotValidClosedAt(request)) {
            errors.rejectValue("closedAt", "invalid.closedAt", "강연 종료일시를 정확히 입력하세요.")
        }
    }

    private fun isNotValidOpenedAt(request: LectureRequest) =
        request.openedAt.isBefore(LocalDateTime.now())

    private fun isNotValidClosedAt(request: LectureRequest) =
        request.closedAt.isBefore(request.openedAt)
}