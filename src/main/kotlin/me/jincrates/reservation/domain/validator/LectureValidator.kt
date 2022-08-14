package me.jincrates.reservation.domain.validator

import me.jincrates.reservation.model.LectureRequest
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

        /*
        //강연 시작일자는 오늘 이전으로도 지정할 수 있도록 하겠다.
        if (isNotValidOpenedAt(request)) {
            errors.rejectValue("openedAt", "invalid.createdAt", "강연 시작일시를 정확히 입력하세요.")
        }
        */
        if (isNotValidClosedAt(request)) {
            errors.rejectValue("closedAt", "invalid.closedAt", "강연 종료일시를 정확히 입력하세요.")
        }
    }

    private fun isNotValidOpenedAt(request: LectureRequest) =
        request.openedAt.isBefore(LocalDateTime.now())

    private fun isNotValidClosedAt(request: LectureRequest) =
        request.closedAt.isBefore(request.openedAt)
}