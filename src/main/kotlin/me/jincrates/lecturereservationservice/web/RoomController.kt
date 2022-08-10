package me.jincrates.lecturereservationservice.web

import me.jincrates.lecturereservationservice.config.AuthUser
import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import me.jincrates.lecturereservationservice.model.RoomRequest
import me.jincrates.lecturereservationservice.service.RoomService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/admin/rooms")  //TODO: 어드민(BackOffice) 권한 가진자만 가능해야할 것으로 보임
class RoomController(
    private val roomService: RoomService,
) {

    @PostMapping()
    fun createRoom(
        authUser: AuthUser, // memberId: Long,  // TODO: 사용자 인증처리가 필요하지 않을까
        @Valid @RequestBody request: RoomRequest,
    ) = roomService.createRoom(authUser.userId, request)

    @GetMapping()
    fun getAll(
        authUser: AuthUser,
        @RequestParam(required = false, defaultValue = "ACTIVE") status: CommonStatus,
    ) = roomService.getAll(status)

    @GetMapping("/{id}")
    fun getRoom(
        authUser: AuthUser,
        @PathVariable id: Long,
    ) = roomService.getRoom(id)

    @PutMapping("/{id}")
    fun updateRoom(
        authUser: AuthUser,
        @PathVariable id: Long,
        @Valid @RequestBody request: RoomRequest,
    ) = roomService.updateRoom(id, request)

    @PutMapping("/{id}/{status}")
    fun updateRoomStatus(
        authUser: AuthUser,
        @PathVariable id: Long,
        @PathVariable status: String,
    ) = roomService.updateRoomStatus(id, status)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRoom(
        authUser: AuthUser,
        @PathVariable id: Long,
    ) = roomService.deleteRoom(id);
}