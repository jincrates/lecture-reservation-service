package me.jincrates.lecturereservationservice.service

import me.jincrates.lecturereservationservice.domain.Lecture
import me.jincrates.lecturereservationservice.domain.LectureRepository
import me.jincrates.lecturereservationservice.domain.RoomRepository
import me.jincrates.lecturereservationservice.exception.NotFoundException
import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.model.LectureResponse
import me.jincrates.lecturereservationservice.model.toResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class LectureService(
    private val lectureRepository: LectureRepository,
    private val roomRepository: RoomRepository,
) {
    /**
     * 강연 등록
     */
    @Transactional
    fun create(roomId: Long, userId: String, request: LectureRequest): LectureResponse {
        val room = roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")

        val lecture = Lecture(
            room = room,
            title = request.title,
            description = request.description,
            lecturerName = request.lecturerName,
            limitOfReservations = request.limitOfReservations,
            openedAt = request.openedAt,
            closedAt = request.closedAt,
        )

        //TODO: 강연장 인원제한 수와 강연 신청인원 수를 비교해야함
        //TODO: 예약마감인원과 강의실 수용인원 비교

        return lectureRepository.save(lecture).toResponse()
    }

    /**
     * 강연 전체 조회
     * TODO: 날짜 조건 추가 필요
     */
    @Transactional(readOnly = true)
    fun getAll(roomId: Long) = lectureRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId)?.map { it.toResponse() }

    fun get(roomId: Long, lectureId: Long): LectureResponse {
        roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        val lecture = lectureRepository.findByIdOrNull(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")
        return lecture.toResponse()
    }



}