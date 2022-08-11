package me.jincrates.lecturereservationservice.domain.validator

import me.jincrates.lecturereservationservice.model.RoomRequest
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class RoomValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return RoomRequest::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val request: RoomRequest = target as RoomRequest

    }
}