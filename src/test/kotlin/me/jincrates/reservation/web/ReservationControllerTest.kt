package me.jincrates.reservation.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.config.location=classpath:application-dev.yml"])
class ReservationControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("예약 신청 - 입력값 정상")
    fun  createReservationSuccessTest() {

    }


}