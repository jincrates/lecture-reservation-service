package me.jincrates.lecturereservationservice.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.jincrates.lecturereservationservice.domain.RoomRepository
import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import me.jincrates.lecturereservationservice.model.RoomRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var roomRepository: RoomRepository

    @Test
    @DisplayName("강연장 등록 - 입력값 정상")
    fun createRoomSuccessTest() {
        val roomRequest = RoomRequest(
            title = "테스트 강연장",
            limitOfPersons = 20,
            status = CommonStatus.ACTIVE,
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)

        mockMvc.perform(post("/api/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.title").value(roomRequest.title))
            .andExpect(jsonPath("\$.limitOfPersons").value(roomRequest.limitOfPersons))
            .andExpect(jsonPath("\$.status").value(roomRequest.status.toString()))
            .andDo(print())

        val room = roomRepository.findByTitle(roomRequest.title)

        assertNotNull(room)
        assertEquals(roomRequest.title, room.title)
        assertEquals(roomRequest.limitOfPersons, room.limitOfPersons)
        assertEquals(roomRequest.status, room.status)
    }

}