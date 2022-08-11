package me.jincrates.lecturereservationservice.service

import me.jincrates.lecturereservationservice.domain.Lecture
import me.jincrates.lecturereservationservice.domain.LectureRepository
import me.jincrates.lecturereservationservice.domain.RoomRepository
import me.jincrates.lecturereservationservice.exception.BadRequestException
import me.jincrates.lecturereservationservice.exception.NotFoundException
import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.model.LectureResponse
import me.jincrates.lecturereservationservice.model.toResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureService(
    private val lectureRepository: LectureRepository,
    private val roomRepository: RoomRepository,
) {
    /**
     * 강연 등록
     */
    @Transactional
    fun createLecture(roomId: Long, createdBy: String, request: LectureRequest): LectureResponse {
        val room = roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")

        if (room.limitOfPersons < request.limitOfReservations) {
            throw BadRequestException("예약 마감인원은 강연장 수용인원보다 클 수 없습니다.")
        }

        if (lectureRepository.existsByRoomIdAndOpenedAtBetween(roomId, request.openedAt, request.closedAt)) {
            throw BadRequestException("해당 시간에 이미 신청된 강의가 존재합니다.")
        }

        val lecture = Lecture(
            room = room,
            title = request.title,
            description = request.description,
            lecturerName = request.lecturerName,
            limitOfReservations = request.limitOfReservations,
            openedAt = request.openedAt,
            closedAt = request.closedAt,
            createdBy = createdBy
        )
        room.lectures.add(lecture)
        return lectureRepository.save(lecture).toResponse()
    }

    /**
     * 강연 전체 조회
     */
    @Transactional(readOnly = true)
    fun getAll(roomId: Long) = lectureRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId)?.map { it.toResponse() }

    @Transactional(readOnly = true)
    fun getLecture(roomId: Long, lectureId: Long): LectureResponse {
        roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        val lecture = lectureRepository.findByIdOrNull(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")
        return lecture.toResponse()
    }

    /**
     * 강연 수정
     * TODO: 강연의 마감인원은 현재 신청인원보다 작을 수 없다.
     */
    @Transactional
    fun updateLecture(lectureId: Long, request: LectureRequest): LectureResponse {
        val lecture = lectureRepository.findByIdOrNull(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")
        return with(lecture) {
            title = request.title
            description = request.description
            lecturerName = request.lecturerName
            limitOfReservations = request.limitOfReservations
            openedAt = request.openedAt
            closedAt = request.closedAt
            lectureRepository.save(this).toResponse()
        }
    }

    @Transactional
    fun deleteLecture(roomId: Long, lectureId: Long) {
        val room = roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        lectureRepository.findByIdOrNull(lectureId)?.let { lecture ->
            room.lectures.remove(lecture)
            lectureRepository.delete(lecture)
        }
    }
}