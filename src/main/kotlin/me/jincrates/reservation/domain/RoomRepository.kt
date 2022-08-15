package me.jincrates.reservation.domain

import me.jincrates.reservation.domain.enums.CommonStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface RoomRepository : JpaRepository<Room, Long> {

    fun findByTitle(title: String): Room?
    fun findAllByStatusOrderByCreatedAtDesc(status: CommonStatus): List<Room>?

    @Query("select distinct r from Room r join fetch r.lectures")
    fun findAllWithLectureUsingFetchJoin(): List<Room>?
}