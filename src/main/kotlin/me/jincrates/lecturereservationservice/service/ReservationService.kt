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
    /**
     * 강연 예약신청
     */
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

    @Transactional(readOnly = true)
    fun getReservationById(reservationId: Long): ReservationResponse {
        val reservation = reservationRepository.findByIdOrNull(reservationId) ?: throw NotFoundException("예약 내역이 없습니다.")
        return reservation.toResponse()
    }

    @Transactional
    fun updateReservation(reservationId: Long, request: ReservationRequest): ReservationResponse? {
        val reservation = reservationRepository.findByIdOrNull(reservationId) ?: throw NotFoundException("예약 내역이 없습니다.")
        return with(reservation) {
            userId = request.userId
            status = request.status!!
            reservationRepository.save(this).toResponse()
        }
    }
    @Transactional
    fun updateStatusToApproval(reservationId: Long, request: ReservationRequest): ReservationResponse? {
        val reservation = reservationRepository.findByIdOrNull(reservationId) ?: throw NotFoundException("예약 내역이 없습니다.")
        return with(reservation) {
            userId = request.userId
            status = ReservationStatus.APPROVAL
            reservationRepository.save(this).toResponse()
        }
    }

    @Transactional
    fun updateStatusToWaiting(reservationId: Long, request: ReservationRequest): ReservationResponse? {
        val reservation = reservationRepository.findByIdOrNull(reservationId) ?: throw NotFoundException("예약 내역이 없습니다.")
        return with(reservation) {
            userId = request.userId
            status = ReservationStatus.WAITING
            reservationRepository.save(this).toResponse()
        }
    }

    @Transactional
    fun updateStatusToCancel(reservationId: Long, request: ReservationRequest): ReservationResponse? {
        val reservation = reservationRepository.findByIdOrNull(reservationId) ?: throw NotFoundException("예약 내역이 없습니다.")
        return with(reservation) {
            userId = request.userId
            status = ReservationStatus.CANCEL
            reservationRepository.save(this).toResponse()
        }
    }

    @Transactional
    fun deleteReservation(lectureId: Long, reservationId: Long) {
        val lecture = lectureRepository.findByIdOrNull(lectureId) ?: throw NotFoundException("강연이 존재하지 않습니다.")
        reservationRepository.findByIdOrNull(reservationId)?.let { reservation ->
            lecture.reservations.remove(reservation)
        }
    }
}