package me.jincrates.lecturereservationservice.domain

import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface RoomRepository : JpaRepository<Room, Long> {
    fun findByTitle(title: String): Room?
    fun findAllByStatusOrderByCreatedAtDesc(status: CommonStatus): List<Room>?
    fun findByIdAndCreatedBy(id: Long, createdBy: String): Room?
}