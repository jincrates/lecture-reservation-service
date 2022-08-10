package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.model.ReservationRequest
import me.jincrates.lecturereservationservice.service.ReservationService
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
}