package me.jincrates.lecturereservationservice.domain.validator

import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.model.LectureResponse
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
        val request : LectureRequest = target as LectureRequest

        if (isNotValidCreatedAt(request)) {
            errors.rejectValue("createdAt", "강연시작일시를 정확히 입력하세요.")
        }

        if (isNotValidClosedAt(request)) {
            errors.rejectValue("closedAt", "강연종료일시를 정확히 입력하세요.")
        }
    }

    private fun isNotValidClosedAt(request: LectureRequest) =
        request.closedAt.isBefore(request.closedAt)

    private fun isNotValidCreatedAt(request: LectureRequest) =
        request.openedAt.isBefore(LocalDateTime.now())
}