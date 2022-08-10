package me.jincrates.lecturereservationservice.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface LectureRepository : JpaRepository<Lecture, Long> {
    fun findByTitle(title: String): Lecture?
    fun findAllByRoomIdOrderByCreatedAtDesc(roomId: Long): List<Lecture>?
}