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

    //TODO: 중복강의 등록 불가 - 동일 시간에 동일 강의실 강연이 있는경우
    //TODO: 강연장 수용인원보다 예약마감인원은 작아야한다.
    @PostMapping()
    fun createLecture(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @Valid @RequestBody request: LectureRequest,
    ) = lectureService.createLecture(roomId, authUser.userId, request)

    //TODO: 날짜 조건 추가 필요: 강연시작시간 1주일 전에 노출, 강연시작 1일 후 노출목록에서 사라짐
    @GetMapping()
    fun getAll(
        authUser: AuthUser,
        @PathVariable roomId: Long,
    ) = lectureService.getAll(roomId)

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