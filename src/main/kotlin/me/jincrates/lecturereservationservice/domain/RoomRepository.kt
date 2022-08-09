package me.jincrates.lecturereservationservice.domain

import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<Room, Long> {
    fun findByTitle(title: String): Room?
    fun findAllByStatusOrderByCreatedAtDesc(status: CommonStatus): List<Room>?

}