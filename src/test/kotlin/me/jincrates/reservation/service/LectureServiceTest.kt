package me.jincrates.reservation.service

import me.jincrates.reservation.domain.LectureRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest
class LectureServiceTest {

    @Autowired lateinit var lectureRepository: LectureRepository

    @Test
    @DisplayName("N+1 테스트")
    fun getLectures() {
        val now = LocalDate.now()
        val fromDate = now.atStartOfDay().minusDays(7)
        val toDate = now.atTime(LocalTime.MAX).plusDays(1)

        val lectures = lectureRepository.findByRoomIdAndOpenedAtBetween(1L, fromDate, toDate )
        println("#############################")
        val fetchLectures = lectureRepository.findByRoomIdAndOpenedAtBetweenUsingFetchJoin(1L, fromDate, toDate)
        println(lectures!!.size.toString())
        println("#############################")
        println(fetchLectures!!.size.toString())
    }
}