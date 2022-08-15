package me.jincrates.reservation.service

import me.jincrates.reservation.domain.Lecture
import me.jincrates.reservation.domain.LectureRepository
import me.jincrates.reservation.domain.RoomRepository
import me.jincrates.reservation.exception.BadRequestException
import me.jincrates.reservation.exception.NotFoundException
import me.jincrates.reservation.model.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

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

        //open at만 조건 처리가 됨
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
    fun getAll(roomId: Long) =
        lectureRepository.findAllByRoomIdWithReservationUsingFetchJoin(roomId)?.map { it.toResponse() }

    /**
     * 강연 전체 조회(기간)
     */
    @Transactional(readOnly = true)
    fun getAllBeforeDaysBetween(roomId: Long, beforeDays: Long, afterDays: Long): List<LectureResponse>? {
        val now = LocalDate.now()
        val fromDate = now.atStartOfDay().minusDays(beforeDays)
        val toDate = now.atTime(LocalTime.MAX).plusDays(afterDays)
        return lectureRepository.findByRoomIdAndOpenedAtBetweenUsingFetchJoin(roomId, fromDate, toDate)
            ?.map { it.toResponse() }
    }

    /**
     * 인기강연 조회
     */
    @Transactional(readOnly = true)
    fun getPopularLecture(roomId: Long): List<LecturePopularResponse>? {
        return lectureRepository.findByRoomIdWithMostPopularFor3days(roomId)
            ?.map { it.toResponsePopular() }
    }

    @Transactional(readOnly = true)
    fun getLecture(roomId: Long, lectureId: Long): LectureResponse {
        roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        val lecture = lectureRepository.findByIdOrNull(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")
        return lecture.toResponse()
    }

    @Transactional(readOnly = true)
    fun getLectureUsers(roomId: Long, lectureId: Long, status: String): LectureUsersResponse {
        roomRepository.findByIdOrNull(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        val lecture = lectureRepository.findByLectureIdWithReservationUsingFetchJoin(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")
        return lecture.toResponseUsers(status)
    }

    /**
     * 강연 수정
     * TODO: 강연의 마감인원은 현재 신청인원보다 작을 수 없다.
     */
    @Transactional
    fun updateLecture(lectureId: Long, request: LectureRequest): LectureResponse {
        val lecture = lectureRepository.findByIdWithPessimisticLock(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")
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
        val room = roomRepository.findByIdWithPessimisticLock(roomId) ?: throw NotFoundException("강연장이 존재하지 않습니다.")
        lectureRepository.findByIdOrNull(lectureId)?.let { lecture ->
            room.lectures.remove(lecture)
            lectureRepository.delete(lecture)
        }
    }
}