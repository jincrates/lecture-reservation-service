package me.jincrates.reservation.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@Transactional(readOnly = true)
interface ReservationRepository : JpaRepository<Reservation, Long> {

    fun existsByLectureIdAndUserId(lectureId: Long, userId: String): Boolean

    fun findByUserId(userId: String): List<Reservation>?
    fun existsByUserId(userId: String): Boolean

    fun findByLectureIdAndUserId(id: Long?, userId: String): Reservation?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reservation r where r.id = :id")
    fun findByIdWithPessimisticLock(id: Long): Reservation?
}