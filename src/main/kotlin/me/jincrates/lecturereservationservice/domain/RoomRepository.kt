package me.jincrates.lecturereservationservice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<Room, Long> {
    fun findByTitle(title: String): Room

}