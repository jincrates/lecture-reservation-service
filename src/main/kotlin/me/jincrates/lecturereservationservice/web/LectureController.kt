package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.domain.Room
import me.jincrates.lecturereservationservice.domain.validator.LectureValidator
import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.model.LectureResponse
import me.jincrates.lecturereservationservice.service.LectureService
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.Errors
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/lectures")
class LectureController(
    private val lectureService: LectureService,
    private val lectureValidator: LectureValidator,
    private val logger: KLogger = KotlinLogging.logger {},
) {

    //지정한 객체로 요청이 들어올 경우 lectureValidator를 우선 실행
    @InitBinder("lectureRequest")
    fun initBinder(webDataBinder: WebDataBinder) {
        logger.info{ "webDataBinder=" + webDataBinder + ", target=" + webDataBinder.target }
        webDataBinder.addValidators(lectureValidator)
    }

    @PostMapping()
    fun createLecture(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @Valid @RequestBody request: LectureRequest,
    ) = lectureService.createLecture(roomId, authUser.userId, request)

    @GetMapping()
    fun getAll(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @RequestParam(required = false, name = "beforeDays", defaultValue = "7") beforeDays: Long,
        @RequestParam(required = false, name = "afterDays", defaultValue = "1") afterDays: Long
    ) = lectureService.getAllBeforeDaysBetween(roomId, beforeDays, afterDays)

    @GetMapping("/{lectureId}")
    fun getLecture(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @PathVariable lectureId: Long,
    ) = lectureService.getLecture(roomId, lectureId)

    @PutMapping("/{lectureId}")
    fun updateLecture(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @Valid @RequestBody request: LectureRequest,
    ) = lectureService.updateLecture(lectureId, request)

    @DeleteMapping("/{lectureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteLecture(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @PathVariable lectureId: Long,
    ) = lectureService.deleteLecture(roomId, lectureId);
}