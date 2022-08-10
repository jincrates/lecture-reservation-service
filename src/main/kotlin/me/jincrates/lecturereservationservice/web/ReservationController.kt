package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.model.ReservationRequest
import me.jincrates.lecturereservationservice.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/lectures/{lectureId}/reservations")
class ReservationController(
    private val reservationService: ReservationService,
) {

    @PostMapping()
    fun createReservation(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.createReservation(lectureId, request)

    @GetMapping("/{reservationId}")
    fun getReservation(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
    ) = reservationService.getReservationById(reservationId)

    @GetMapping("/{reservationId}/users/{userId}")
    fun getReservationByUserId(
        authUser: AuthUser,
        @PathVariable userId: String,
    ) = reservationService.getReservationByUserId(userId)

    @PutMapping("/{reservationId}")
    fun updateReservation(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateReservation(reservationId, request)

    @PutMapping("/{reservationId}/approval")
    fun updateStatusToApproval(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateStatusToApproval(reservationId, request)

    @PutMapping("/{reservationId}/waiting")
    fun updateStatusToWaiting(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateStatusToWaiting(reservationId, request)

    @PutMapping("/{reservationId}/cancel")
    fun updateStatusToCancel(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateStatusToCancel(reservationId, request)

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteReservation(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @PathVariable reservationId: Long,
    ) = reservationService.deleteReservation(lectureId, reservationId)
}