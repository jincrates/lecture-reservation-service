package me.jincrates.lecturereservationservice.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface ReservationRepository : JpaRepository<Reservation, Long> {

    fun existsByLectureIdAndUserId(lectureId: Long, userId: String): Boolean

    fun findByLectureIdAndUserId(lectureId: Long, userId: String): Reservation?
}