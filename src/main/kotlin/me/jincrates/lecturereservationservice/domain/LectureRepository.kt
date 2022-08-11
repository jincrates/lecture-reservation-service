package me.jincrates.lecturereservationservice.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
interface LectureRepository : JpaRepository<Lecture, Long> {

    fun findByTitle(title: String): Lecture?

    fun findAllByRoomIdOrderByCreatedAtDesc(roomId: Long): List<Lecture>?

    fun existsByRoomIdAndOpenedAtBetween(roomId: Long, openedAt: LocalDateTime, closedAt: LocalDateTime): Boolean

    fun findByRoomIdAndOpenedAtBetween(roomId: Long, fromDate: LocalDateTime, toDate: LocalDateTime): List<Lecture>?

}