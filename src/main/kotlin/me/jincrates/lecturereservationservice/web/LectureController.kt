package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.domain.Room
import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.service.LectureService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/lectures")
class LectureController(
    private val lectureService: LectureService,
) {

    @PostMapping()
    fun create(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @Valid @RequestBody request: LectureRequest,
    ) = lectureService.create(roomId, authUser.userId, request)


}