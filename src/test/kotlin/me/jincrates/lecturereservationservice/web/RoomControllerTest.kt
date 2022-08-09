package me.jincrates.lecturereservationservice.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.jincrates.lecturereservationservice.domain.RoomRepository
import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import me.jincrates.lecturereservationservice.model.RoomRequest
import org.hibernate.validator.constraints.Length
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
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

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
        assertEquals(roomRequest.title, room?.title)
        assertEquals(roomRequest.limitOfPersons, room?.limitOfPersons)
        assertEquals(roomRequest.status, room?.status)
    }

    // 강연장명 입력하지 않음
    // 강연장명 50자 제한
    // 강연장 수용인원 1명 이상
    // 상태값 없어도 정상처리 되는지
    @Test
    @DisplayName("강연장 등록 - 입력값 오류1 - 강연장명 입력하지 않음")
    fun createRoomFailTest1() {
        val roomRequest = RoomRequest(
            title = "",
            limitOfPersons = 20,
            status = CommonStatus.ACTIVE,
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)

        mockMvc.perform(post("/api/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(500))
            .andExpect(jsonPath("\$.message").value("강연장명을 입력하지 않았습니다."))
            .andDo(print())

        val room = roomRepository.findByTitle(roomRequest.title)
        assertNull(room)
    }

    @Test
    @DisplayName("강연장 등록 - 입력값 오류2 - 강연장명 50자 초과")
    fun createRoomFailTest2() {
        val roomRequest = RoomRequest(
            title = "원대하고, 이는 것은 작고 목숨이 뿐이다. 있을 같은 얼마나 하는 끝에 곳이 구하지 봄바람이다. 되는 영락과 풀이 시들어 꽃이 청춘은 피어나기 봄바람이다. 그들에게 투명하되 얼마나 관현악이며, 밝은 뜨거운지라, 바이며, 인간은 미묘한 이것이다. 생명을 얼마나 우리의 교향악이다. 아니한 소담스러운 사는가 이성은 때에, 목숨이 황금시대다. 힘차게 얼음에 천하를 인간이 갑 동산에는 많이 위하여서. 장식하는 있는 것은 살 청춘을 하는 밥을 낙원을 끓는 것이다. 설산에서 들어 구하지 철환하였는가? 위하여, 천하를 이 간에 생의 쓸쓸한 품었기 황금시대다.",
            limitOfPersons = 20,
            status = CommonStatus.ACTIVE,
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)

        mockMvc.perform(post("/api/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(500))
            .andExpect(jsonPath("\$.message").value("강연장명은 50자까지만 입력할 수 있습니다."))
            .andDo(print())

        val room = roomRepository.findByTitle(roomRequest.title)
        assertNull(room)
    }

    @Test
    @DisplayName("강연장 등록 - 입력값 오류3 - 강연장 수용인원 0명")
    fun createRoomFailTest3() {
        val roomRequest = RoomRequest(
            title = "테스트 강연장",
            limitOfPersons = 0,
            status = CommonStatus.ACTIVE,
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)

        mockMvc.perform(post("/api/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(500))
            .andExpect(jsonPath("\$.message").value("강연장 수용인원은 1명 이상입니다."))
            .andDo(print())

        val room = roomRepository.findByTitle(roomRequest.title)
        assertNull(room)
    }

}