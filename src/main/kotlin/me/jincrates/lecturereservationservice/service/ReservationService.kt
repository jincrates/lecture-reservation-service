package me.jincrates.lecturereservationservice.service

import me.jincrates.lecturereservationservice.domain.LectureRepository
import me.jincrates.lecturereservationservice.domain.Reservation
import me.jincrates.lecturereservationservice.domain.ReservationRepository
import me.jincrates.lecturereservationservice.domain.enums.ReservationStatus
import me.jincrates.lecturereservationservice.exception.BadRequestException
import me.jincrates.lecturereservationservice.exception.NotFoundException
import me.jincrates.lecturereservationservice.model.ReservationRequest
import me.jincrates.lecturereservationservice.model.ReservationResponse
import me.jincrates.lecturereservationservice.model.toResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReservationService(
    private val lectureRepository: LectureRepository,
    private val reservationRepository: ReservationRepository,
) {
    @Transactional
    fun createReservation(lectureId: Long, request: ReservationRequest): ReservationResponse {
        val lecture = lectureRepository.findByIdOrNull(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")

        if (reservationRepository.existsByLectureIdAndUserId(lectureId, request.userId)) {
            throw BadRequestException("이미 강연을 신청하셨습니다.")
        }

        val status = if (lecture.reservations.size < lecture.limitOfReservations) {
            ReservationStatus.APPROVAL
        } else {
            ReservationStatus.WAITING
        }

        val reservation = Reservation(
            lecture = lecture,
            userId = request.userId,
            reservedAt = LocalDateTime.now(),
            status = status,
        )
        lecture.reservations.add(reservation)
        return reservationRepository.save(reservation).toResponse()
    }
}