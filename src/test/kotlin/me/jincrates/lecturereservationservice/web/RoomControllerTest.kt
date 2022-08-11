package me.jincrates.lecturereservationservice.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.jincrates.lecturereservationservice.domain.Lecture
import me.jincrates.lecturereservationservice.domain.LectureRepository
import me.jincrates.lecturereservationservice.domain.Room
import me.jincrates.lecturereservationservice.domain.RoomRepository
import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.model.RoomRequest
import me.jincrates.lecturereservationservice.service.LectureService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.config.location=classpath:application-dev.yml"])
class RoomControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var lectureRepository: LectureRepository
    @Autowired lateinit var lectureService: LectureService

    @BeforeEach
    fun beforeEach() {
        createRooms()  //강연장 15개를 생성
    }

    @AfterEach()
    fun afterEach() {
        roomRepository.deleteAll()
    }

    @Test
    @DisplayName("강연장 등록 - 입력값 정상")
    fun createRoomSuccessTest() {
        val roomRequest = RoomRequest(
            title = "테스트 강연장",
            limitOfPersons = 20,
            status = CommonStatus("ACTIVE").toString(),
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)
        mockMvc.perform(post("/api/v1/admin/rooms")
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
        assertEquals(roomRequest.status, room?.status.toString())
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
            status = CommonStatus("ACTIVE").toString(),
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)

        mockMvc.perform(post("/api/v1/admin/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(400))
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
            status = CommonStatus("ACTIVE").toString(),
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)

        mockMvc.perform(post("/api/v1/admin/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(400))
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
            status = CommonStatus("ACTIVE").toString(),
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)

        mockMvc.perform(post("/api/v1/admin/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("\$.message").value("강연장 수용인원은 1명 이상입니다."))
            .andDo(print())

        val room = roomRepository.findByTitle(roomRequest.title)
        assertNull(room)
    }

    @Test
    @DisplayName("강연장 전체 조회 - 기본값(ACTIVE)")
    fun getAllRoom() {
        val rooms = roomRepository.findAllByStatusOrderByCreatedAtDesc(CommonStatus.ACTIVE)
        assertNotNull(rooms)
        assertEquals(10, rooms!!.size)

        mockMvc.perform(get("/api/v1/admin/rooms"))
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("강연장 전체 조회 - INACTIVE")
    fun getAllRoomInactive() {
        val rooms = roomRepository.findAllByStatusOrderByCreatedAtDesc(CommonStatus.INACTIVE)
        assertNotNull(rooms)
        assertEquals(5, rooms!!.size)

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("status", "INACTIVE")

        mockMvc.perform(get("/api/v1/admin/rooms").queryParams(queryParams))
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("강연장 상세 조회")
    fun getRoom() {
        val roomId: Long = 12
        val room = roomRepository.findById(roomId)
        assertNotNull(room)

        mockMvc.perform(get("/api/v1/admin/rooms/$roomId"))
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("강연장 수정 - 입력값 정상")
    fun editRoomSuccess() {
        val roomId: Long = 1
        val roomRequest = RoomRequest(
            title = "테스트 강연장 - 수정",
            limitOfPersons = 30,
            status = CommonStatus("ACTIVE").toString(),
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)
        println(json)
        mockMvc.perform(put("/api/v1/admin/rooms/$roomId")
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
        assertEquals(roomRequest.status, room?.status.toString())
    }

    @Test
    @DisplayName("강연장 수정 - 입력값 오류1 - id 없음")
    fun editRoomFail1() {
        val roomId: Long = 99
        val roomRequest = RoomRequest(
            title = "테스트 강연장 - 수정",
            limitOfPersons = 30,
            status = CommonStatus("ACTIVE").toString(),
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)
        mockMvc.perform(put("/api/v1/admin/rooms/$roomId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(404))
            .andExpect(jsonPath("\$.message").value("강연장이 존재하지 않습니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("강연장 수정 - 입력값 오류2 - 이미 신청한 강연의 마감인원보다 강연장 수용인원을 적게 수정할 수 없다.")
    fun editRoomFail2() {
        val roomId: Long = 2
        val room = roomRepository.findByIdOrNull(roomId)
        assertNotNull(room)

        val lectureRequest = LectureRequest(
            title = "강연 제목입니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = room!!.limitOfPersons,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )
        val createLecture = lectureService.createLecture(roomId, "94042", lectureRequest)
        assertNotNull(createLecture)

        val limitOfReservationsDown = createLecture.limitOfReservations - 10
        val roomRequest = RoomRequest(
            title = "테스트 강연장 - 수정",
            limitOfPersons = limitOfReservationsDown,
            status = CommonStatus("ACTIVE").toString(),
        )
        val json = jacksonObjectMapper().writeValueAsString(roomRequest)
        mockMvc.perform(put("/api/v1/admin/rooms/$roomId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(400))
            .andExpect(jsonPath("\$.message").value("강연장 수용인원은 강연의 예약마감인원보다 커야합니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("강연장 삭제")
    fun deleteRoom() {
        val roomId: Long = 10
        mockMvc.perform(delete("/api/v1/admin/rooms/$roomId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andDo(print())

        val room = roomRepository.existsById(roomId)
        assertFalse(room)
    }

    fun createRooms() {
        val rooms = mutableListOf<Room>()
        var room: Room
        (1..15).forEach {
            room = if (it < 11) {
                Room(
                    title = "테스트 강연장$it",
                    limitOfPersons = 20 + it,
                    status = CommonStatus.ACTIVE,
                    createdBy = "94042",
                )
            } else {
                Room(
                    title = "테스트 강연장$it",
                    limitOfPersons = 20 + it,
                    status = CommonStatus.INACTIVE,
                    createdBy = "94042",
                )
            }
            rooms.add(room)
        }

        roomRepository.saveAll(rooms)
    }
}