package me.jincrates.reservation.service

import me.jincrates.reservation.domain.RoomRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RoomServiceTest {

    @Autowired lateinit var roomRepository: RoomRepository

    @Test
    @DisplayName("N+1 테스트 - 쿼리 확인")
    fun getRooms() {
        val rooms = roomRepository.findAllWithLectureUsingFetchJoin()
        println(rooms)
    }
}