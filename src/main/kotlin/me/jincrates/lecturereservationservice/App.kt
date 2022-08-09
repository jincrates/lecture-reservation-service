package me.jincrates.lecturereservationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LectureReservationServiceApplication

fun main(args: Array<String>) {
	runApplication<LectureReservationServiceApplication>(*args)
}
