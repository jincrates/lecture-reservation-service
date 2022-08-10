package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.domain.Room
import me.jincrates.lecturereservationservice.domain.validator.LectureValidator
import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.service.LectureService
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
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
    private val lectureValidator: LectureValidator,
) {

    @InitBinder("request")
    fun initBinder(webDataBinder: WebDataBinder) = webDataBinder.addValidators(lectureValidator)

    @PostMapping()
    fun create(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @Valid @RequestBody request: LectureRequest,
    ) = lectureService.create(roomId, authUser.userId, request)

    @GetMapping()
    fun getAll(
        authUser: AuthUser,
        @PathVariable roomId: Long,
    ) = lectureService.getAll(roomId)

    @GetMapping("/{lectureId}")
    fun get(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @PathVariable lectureId: Long,
    ) = lectureService.get(roomId, lectureId)

}