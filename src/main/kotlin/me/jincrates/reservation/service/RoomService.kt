package me.jincrates.reservation.service

import me.jincrates.reservation.domain.Room
import me.jincrates.reservation.domain.RoomRepository
import me.jincrates.reservation.domain.enums.CommonStatus
import me.jincrates.reservation.exception.BadRequestException
import me.jincrates.reservation.exception.NotFoundException
import me.jincrates.reservation.model.RoomRequest
import me.jincrates.reservation.model.RoomResponse
import me.jincrates.reservation.model.toResponse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(
    private val roomRepository: RoomRepository,
    private val logger: KLogger = KotlinLogging.logger {},
) {

    /**
     * 강연장 등록
     */
    @Transactional
    fun createRoom(createdBy: String, request: RoomRequest): RoomResponse {
        val room = Room(
            title = request.title,
            limitOfPersons = request.limitOfPersons,
            status = CommonStatus(request.status!!),
            createdBy = createdBy,
        )
        return roomRepository.save(room).toResponse()
    }

    /**
     * 강연장 전체 조회
     */
    @Transactional(readOnly = true)
    fun getAll(status: CommonStatus) =
        roomRepository.findAllWithLectureUsingFetchJoin()?.map { it.toResponse() } //맵을 통해 Room를 RoomResponse로 변환

    /**
     * 강연장 상세 조회
     */
    @Transactional(readOnly = true)
    fun getRoom(id: Long): RoomResponse {
        val room = roomRepository.findByIdWithLectureUsingFetchJoin(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        return room.toResponse()
    }

    /**
     * 강연장 수정
     */
    @Transactional
    fun updateRoom(id: Long, request: RoomRequest): RoomResponse? {
        val room = roomRepository.findByIdWithPessimisticLock(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")

        //강연장 수용인원
        room.lectures.forEach {
            if (it.limitOfReservations > request.limitOfPersons) {
                throw BadRequestException("강연장 수용인원은 강연의 예약마감인원보다 커야합니다.")
            }
        }

        return with(room) {
            this.title = request.title
            this.limitOfPersons = request.limitOfPersons
            this.status = CommonStatus(request.status!!)
            roomRepository.save(this).toResponse()
        }
    }

    /**
     * 강연장 상태변경
     */
    @Transactional
    fun updateRoomStatus(id: Long, status: String): RoomResponse {
        val room = roomRepository.findByIdWithPessimisticLock(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        return with(room) {
            this.status = CommonStatus(status)
            roomRepository.save(this).toResponse()
        }
    }

    /**
     * 강연장 삭제
     */
    @Transactional
    fun deleteRoom(id: Long) {
        val room = roomRepository.findByIdWithPessimisticLock(id) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        roomRepository.deleteById(id)
    }
}