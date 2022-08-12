package me.jincrates.reservation.web

import io.swagger.annotations.*
import me.jincrates.reservation.config.AuthUser
import me.jincrates.reservation.domain.validator.LectureValidator
import me.jincrates.reservation.model.LectureRequest
import me.jincrates.reservation.service.LectureService
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["02. 강연 관리"], description = "LectureController CRUD 구현")
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

    @ApiOperation(value = "강연 등록", notes = "LectureRequest를 통해 강연을 등록합니다.")
    @ApiImplicitParams(*[
        ApiImplicitParam(name = "roomId", value = "강연장 ID"),
    ])
    @PostMapping()
    fun createLecture(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @Valid @RequestBody request: LectureRequest,
    ) = lectureService.createLecture(roomId, authUser.authId, request)

    @ApiOperation(value = "강연 전체 조회", notes = "강연 전체 목록을 조회합니다. 조회기간 조건에 따라 조회 범위를 지정할 수 있습니다. ex) 강의 시작일자 기준: 7일 전 ~ 1일 후까지의 강연 리스트 조회")
    @ApiImplicitParams(*[
        ApiImplicitParam(name = "roomId", value = "강연장 ID"),
        ApiImplicitParam(name = "beforeDays", value = "조회기간 시작조건", defaultValue = "7"),
        ApiImplicitParam(name = "afterDays", value = "조회기간 종료조건", defaultValue = "1"),
    ])
    @GetMapping()
    fun getAll(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @RequestParam(required = false, name = "beforeDays", defaultValue = "7") beforeDays: Long,
        @RequestParam(required = false, name = "afterDays", defaultValue = "1") afterDays: Long
    ) = lectureService.getAllBeforeDaysBetween(roomId, beforeDays, afterDays)

    @ApiOperation(value = "강연 상세 조회", notes = "강연의 ID를 통해 강연의 상세 정보를 조회합니다.")
    @ApiImplicitParams(*[
        ApiImplicitParam(name = "roomId", value = "강연장 ID"),
        ApiImplicitParam(name = "lectureId", value = "강연 ID"),
    ])
    @GetMapping("/{lectureId}")
    fun getLecture(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @PathVariable lectureId: Long,
    ) = lectureService.getLecture(roomId, lectureId)

    @ApiOperation(value = "강연 신청자 사번 조회", notes = "강연의 ID를 통해 강연 신청자 사번을 조회합니다.")
    @ApiImplicitParams(*[
        ApiImplicitParam(name = "roomId", value = "강연장 ID"),
        ApiImplicitParam(name = "lectureId", value = "강연 ID"),
    ])
    @GetMapping("/{lectureId}/users")
    fun getLectureUsers(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @PathVariable lectureId: Long,
    ) = lectureService.getLectureUsers(roomId, lectureId)

    @ApiOperation(value = "강연 수정", notes = "강연의 정보를 수정합니다.")
    @ApiImplicitParam(name = "lectureId", value = "강연장 ID")
    @PutMapping("/{lectureId}")
    fun updateLecture(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @Valid @RequestBody request: LectureRequest,
    ) = lectureService.updateLecture(lectureId, request)

    @ApiOperation(value = "강연 삭제", notes = "강연의 ID를 통해 강연 정보를 삭제합니다.")
    @ApiImplicitParam(name = "lectureId", value = "강연 ID")
    @DeleteMapping("/{lectureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteLecture(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @PathVariable lectureId: Long,
    ) = lectureService.deleteLecture(roomId, lectureId);
}