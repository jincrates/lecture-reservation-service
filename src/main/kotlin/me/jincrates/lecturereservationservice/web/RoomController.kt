package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.model.RoomRequest
import me.jincrates.lecturereservationservice.service.RoomService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/rooms")
class RoomController(
    private val roomService: RoomService,
) {

    @PostMapping()
    fun create(
        authUser: AuthUser, // memberId: Long,  // TODO: 사용자 인증처리가 필요하지 않을까
        @Valid @RequestBody request: RoomRequest,
    ) = roomService.create(authUser.userCode, request)

}