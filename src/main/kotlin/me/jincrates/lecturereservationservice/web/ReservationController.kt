package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.model.ReservationRequest
import me.jincrates.lecturereservationservice.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/lectures/{lectureId}")
class ReservationController(
    private val reservationService: ReservationService,
) {

    @PostMapping("/reservation")
    fun createReservation(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.createReservation(lectureId, request)

    @GetMapping("/reservation/{reservationId}")
    fun getReservation(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
    ) = reservationService.getReservationById(reservationId)

    @PutMapping("/reservation/{reservationId}")
    fun updateReservation(
        authUser: AuthUser,
        @PathVariable reservationId: Long,
        @Valid @RequestBody request: ReservationRequest,
    ) = reservationService.updateReservation(reservationId, request)

    @DeleteMapping("/reservation/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteReservation(
        authUser: AuthUser,
        @PathVariable lectureId: Long,
        @PathVariable reservationId: Long,
    ) = reservationService.deleteReservation(lectureId, reservationId)
}