package me.jincrates.reservation.web

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import me.jincrates.reservation.config.AuthUser
import me.jincrates.reservation.model.ReservationRequest
import me.jincrates.reservation.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(description = "예약 관리")
@RestController
@RequestMapping("/api/v1/lectures/{lectureId}/reservations")
class ReservationController(
    private val reservationService: ReservationService,
) {

    @ApiOperation(value = "예약 등록", notes = "ReservationRequest를 통해 예약을 등록합니다.")
    @ApiImplicitParam(name = "lectureId", value = "강연 ID", dataType = "integer")
    @PostMapping()
    fun createReservation(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @Valid @RequestBody request: ReservationRequest,  //사번
    ) = reservationService.createReservation(lectureId, request)

    @ApiOperation(value = "예약 상세 조회", notes = "예약의 ID를 통해 예약의 상세 정보를 조회합니다.")
    @ApiImplicitParam(name = "reservationId", value = "예약 ID", dataType = "integer")
    @GetMapping("/{reservationId}")
    fun getReservation(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
    ) = reservationService.getReservationById(reservationId)

    @ApiOperation(value = "예약 조회(신청자 사번으로 조회)", notes = "신청자 사번을 통해 예약 정보를 조회합니다.")
    @ApiImplicitParam(name = "userId", value = "신청자 사번", dataType = "string")
    @GetMapping("/{reservationId}/users/{userId}")
    fun getReservationByUserId(
        authUser: AuthUser,
        @PathVariable userId: String,
    ) = reservationService.getReservationByUserId(userId)

    @ApiOperation(value = "예약 수정", notes = "예약의 정보를 수정합니다.")
    @ApiImplicitParam(name = "reservationId", value = "예약 ID", dataType = "integer")
    @PutMapping("/{reservationId}")
    fun updateReservation(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateReservation(reservationId, request)

    @ApiOperation(value = "예약 승인", notes = "예약의 상태를 승인으로 수정합니다.")
    @ApiImplicitParam(name = "reservationId", value = "예약 ID", dataType = "integer")
    @PutMapping("/{reservationId}/approval")
    fun updateStatusToApproval(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateStatusToApproval(reservationId, request)

    @PutMapping("/{reservationId}/waiting")
    @ApiOperation(value = "예약 대기", notes = "예약의 상태를 대기로 수정합니다.")
    @ApiImplicitParam(name = "reservationId", value = "예약 ID", dataType = "integer")
    fun updateStatusToWaiting(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateStatusToWaiting(reservationId, request)

    @ApiOperation(value = "예약 취소", notes = "예약의 상태를 취소로 수정합니다.")
    @ApiImplicitParam(name = "reservationId", value = "예약 ID", dataType = "integer")
    @PutMapping("/{reservationId}/cancel")
    fun updateStatusToCancel(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateStatusToCancel(reservationId, request)

    @ApiOperation(value = "예약 삭제", notes = "예약의 ID를 통해 예약 정보를 삭제합니다.")
    @ApiImplicitParam(name = "reservationId", value = "예약 ID", dataType = "integer")
    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteReservation(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @PathVariable reservationId: Long,
    ) = reservationService.deleteReservation(lectureId, reservationId)
}