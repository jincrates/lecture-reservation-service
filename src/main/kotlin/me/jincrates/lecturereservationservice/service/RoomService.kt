package me.jincrates.lecturereservationservice.service

import me.jincrates.lecturereservationservice.domain.Room
import me.jincrates.lecturereservationservice.domain.RoomRepository
import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import me.jincrates.lecturereservationservice.exception.NotFoundException
import me.jincrates.lecturereservationservice.model.RoomRequest
import me.jincrates.lecturereservationservice.model.RoomResponse
import me.jincrates.lecturereservationservice.model.toResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(
    private val roomRepository: RoomRepository,
) {

    /**
     * 강연장 등록
     * TODO: 강연장 등록자 정보를 추가해야할까?
     */
    @Transactional
    fun createRoom(userId: String, request: RoomRequest): RoomResponse {
        val room = Room(
            title = request.title,
            limitOfPersons = request.limitOfPersons,
            status = request.status,
        )
        return roomRepository.save(room).toResponse()
    }

    /**
     * 강연장 전체 조회
     */
    @Transactional(readOnly = true)
    fun getAll(status: CommonStatus) =
        roomRepository.findAllByStatusOrderByCreatedAtDesc(status)?.map { it.toResponse() } //맵을 통해 Room를 RoomResponse로 변환

    /**
     * 강연장 상세 조회
     */
    @Transactional(readOnly = true)
    fun getRoom(id: Long): RoomResponse {
        val room = roomRepository.findByIdOrNull(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        return room.toResponse()
    }

    /**
     * 강연장 수정
     */
    @Transactional
    fun updateRoom(userId: String, id: Long, request: RoomRequest): RoomResponse {
        val room = roomRepository.findByIdOrNull(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        return with(room) {
            title = request.title
            limitOfPersons = request.limitOfPersons
            status = request.status
            roomRepository.save(this).toResponse()
        }
    }

    /**
     * 강연장 상태변경
     */
    @Transactional
    fun updateRoomStatus(id: Long, status: String): RoomResponse {
        val room = roomRepository.findByIdOrNull(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        room.status = CommonStatus(status)
        return roomRepository.save(room).toResponse()
    }

    /**
     * 강연장 삭제
     */
    @Transactional
    fun deleteRoom(id: Long) {
        val room = roomRepository.findByIdOrNull(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        roomRepository.deleteById(id)
    }
}