package me.jincrates.reservation.web

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import me.jincrates.reservation.config.AuthUser
import me.jincrates.reservation.domain.enums.CommonStatus
import me.jincrates.reservation.model.RoomRequest
import me.jincrates.reservation.service.RoomService
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

@Api(tags = ["01. 강연장 관리"], description = "RoomController CRUD 구현")
@RestController
@RequestMapping("/api/v1/admin/rooms")  //TODO: 어드민(BackOffice) 권한 가진자만 가능해야할 것으로 보임
class RoomController(
    private val roomService: RoomService,
) {

    @ApiOperation(value = "강연장 등록", notes = "RoomRequest를 통해 강연장을 등록합니다.")
    @PostMapping()
    fun createRoom(
        authUser: AuthUser,
        @Valid @RequestBody request: RoomRequest,
    ) = roomService.createRoom(authUser.authId, request)

    @ApiOperation(value = "강연장 전체 조회", notes = "강연장 전체 목록을 조회합니다.")
    @ApiImplicitParam(name = "status", value = "강연장 상태")
    @GetMapping()
    fun getAll(
        authUser: AuthUser,
        @RequestParam(required = false, defaultValue = "ACTIVE") status: CommonStatus,
    ) = roomService.getAll(status)

    @ApiOperation(value = "강연장 상세 조회", notes = "강연장의 ID를 통해 강연장의 상세 정보를 조회합니다.")
    @ApiImplicitParam(name = "roomId", value = "강연장 ID")
    @GetMapping("/{roomId}")
    fun getRoom(
        authUser: AuthUser,
        @PathVariable roomId: Long,
    ) = roomService.getRoom(roomId)

    //TODO: 강연장 인원 수정시 강연의 마감인원 보다 적게 수정할 수 없다.
    @ApiOperation(value = "강연장 수정", notes = "강연장의 정보를 수정합니다.")
    @ApiImplicitParam(name = "roomId", value = "강연장 ID")
    @PutMapping("/{roomId}")
    fun updateRoom(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @Valid @RequestBody request: RoomRequest,
    ) = roomService.updateRoom(roomId, request)

    @ApiOperation(value = "강연장 상태 수정", notes = "강연장의 상태값을 수정합니다.")
    @ApiImplicitParams(*[
        ApiImplicitParam(name = "roomId", value = "강연장 ID"),
        ApiImplicitParam(name = "status", value = "강연장 상태"),
    ])
    @PutMapping("/{roomId}/{status}")
    fun updateRoomStatus(
        authUser: AuthUser,
        @PathVariable roomId: Long,
        @PathVariable status: String,
    ) = roomService.updateRoomStatus(roomId, status)

    @ApiOperation(value = "강연장 삭제", notes = "강연장의 ID를 통해 강연장 정보를 삭제합니다.")
    @ApiImplicitParam(name = "roomId", value = "강연장 ID")
    @DeleteMapping("/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRoom(
        authUser: AuthUser,
        @PathVariable roomId: Long,
    ) = roomService.deleteRoom(roomId);
}