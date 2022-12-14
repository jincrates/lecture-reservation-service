package me.jincrates.reservation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class LectureReservationServiceApplication

fun main(args: Array<String>) {
	runApplication<LectureReservationServiceApplication>(*args)
}
