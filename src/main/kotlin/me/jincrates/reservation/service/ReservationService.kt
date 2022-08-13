package me.jincrates.reservation.service

import me.jincrates.reservation.domain.LectureRepository
import me.jincrates.reservation.domain.Reservation
import me.jincrates.reservation.domain.ReservationRepository
import me.jincrates.reservation.domain.enums.ReservationStatus
import me.jincrates.reservation.exception.BadRequestException
import me.jincrates.reservation.exception.NotFoundException
import me.jincrates.reservation.model.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
            throw BadRequestException("이미 신청하신 강연입니다.")
        }

        //TODO: lecture.reservations.size가 아니라 approval 상태의 reservaiotns 카운트를 가져와야한다.
        //강연신청시 자동으로 승인되며, 예약 마감인원을 넘어갔을 경우 대기상태로 등록된다.
        val status = if (lecture.reservations.size < lecture.limitOfReservations) {
            ReservationStatus.APPROVAL
        } else {
            ReservationStatus.WAITING
        }

        val reservation = Reservation(
            lecture = lecture,
            userId = request.userId,
            status = status,
        )
        lecture.reservations.add(reservation)
        return reservationRepository.save(reservation).toResponse()
    }

    /**
     * 예약 상세 조회
     */
    @Transactional(readOnly = true)
    fun getReservationById(reservationId: Long): ReservationResponse {
        val reservation = reservationRepository.findByIdOrNull(reservationId) ?: throw NotFoundException("예약 내역이 없습니다.")
        return reservation.toResponse()
    }

    /**
     * 예약 조회(사번)
     * 사번으로 신청한 강의 목록을 볼 수 있다.
     */
    @Transactional(readOnly = true)
    fun getReservationByUserId(userId: String): List<ReservationDetailResponse> {
        val reservation = if (reservationRepository.existsByUserId(userId)) {
            reservationRepository.findByUserId(userId)
        } else {
            throw NotFoundException("예약 내역이 없습니다.")
        }

        return reservation!!.map { it.toDetailResponse() }
    }

    @Transactional
    fun updateReservation(reservationId: Long, request: ReservationRequest): ReservationResponse? {
        val reservation = reservationRepository.findByIdOrNull(reservationId) ?: throw NotFoundException("예약 내역이 없습니다.")
        return with(reservation) {
            userId = request.userId
            status = ReservationStatus(request.status!!)
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
            reservationRepository.delete(reservation)
        }
    }


}