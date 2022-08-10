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
    fun create(roomId: Long, userId: String, request: LectureRequest): LectureResponse {
        val room = roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")

        val lecture = Lecture(
            room = room,
            title = request.title,
            description = request.description,
            lecturerName = request.lecturerName,
            numberOfReservations = request.numberOfReservations,
            openedAt = LocalDateTime.parse(request.openedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            closedAt = LocalDateTime.parse(request.closedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        )

        //강연장 인원제한 수와 강연 신청인원 수를 비교해야함

        return lectureRepository.save(lecture).toResponse()
    }
}