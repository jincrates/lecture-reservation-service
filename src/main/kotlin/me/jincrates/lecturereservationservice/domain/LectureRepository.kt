package me.jincrates.lecturereservationservice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository : JpaRepository<Lecture, Long> {
}