package me.jincrates.reservation.web

import com.fasterxml.jackson.databind.ObjectMapper
import me.jincrates.reservation.domain.Lecture
import me.jincrates.reservation.domain.LectureRepository
import me.jincrates.reservation.domain.Room
import me.jincrates.reservation.domain.RoomRepository
import me.jincrates.reservation.domain.enums.CommonStatus
import me.jincrates.reservation.model.LectureRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.config.location=classpath:application-dev.yml"])
class LectureControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper
    @Autowired lateinit var lectureRepository: LectureRepository
    @Autowired lateinit var roomRepository: RoomRepository

    @Test
    @DisplayName("강연 등록 - 입력값 정상")
    fun createLectureSuccessTest() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = createLectureRequest()
        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value(lectureRequest.title))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(lectureRequest.description))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.lecturerName").value(lectureRequest.lecturerName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.limitOfReservations").value(lectureRequest.limitOfReservations))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.openedAt").value(lectureRequest.openedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.closedAt").value(lectureRequest.closedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNotNull(lecture)
        assertEquals(lectureRequest.title, lecture?.title)
        assertEquals(lectureRequest.description, lecture?.description)
        assertEquals(lectureRequest.lecturerName, lecture?.lecturerName)
        assertEquals(lectureRequest.limitOfReservations, lecture?.limitOfReservations)
        assertEquals(
            lectureRequest.openedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            lecture?.openedAt?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        assertEquals(
            lectureRequest.closedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            lecture?.closedAt?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
    }

    @Test
    @DisplayName("강연 등록 - 입력값 오류1 - 강연 제목 입력하지 않음")
    fun createLectureFailTest1() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = LectureRequest(
            title = "",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )
        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("강연 제목을 입력하지 않았습니다."))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNull(lecture)
    }

    @Test
    @DisplayName("강연 등록 - 입력값 오류2 - 강연 제목 50자 초과")
    fun createLectureFailTest2() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = LectureRequest(
            title = "이름과, 무엇인지 하나에 나는 강아지, 하늘에는 흙으로 가득 차 버리었습니다. 말 새워 이국 잔디가 밤을 아직 덮어 하늘에는 않은 봅니다. 보고, 못 써 이네들은 지나가는 계십니다. 피어나듯이 새워 둘 까닭입니다. 아무 밤을 이런 당신은 노새, 잠, 청춘이 봅니다. 내 북간도에 별 흙으로 하나에 덮어 있습니다. 덮어 나의 당신은 별 까닭입니다. 차 가을로 어머니, 마리아 경, 그리워 봅니다. 새겨지는 없이 한 다 어머님, 사람들의 봅니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )
        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("강연 제목은 50자까지만 입력할 수 있습니다."))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNull(lecture)
    }

    @Test
    @DisplayName("강연 등록 - 입력값 오류3 - 강연 상세내용 입력하지 않음")
    fun createLectureFailTest3() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = LectureRequest(
            title = "강연 제목입니다.",
            description = "",
            lecturerName = "강연자",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )
        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("강연 상세내용을 입력하지 않았습니다."))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNull(lecture)
    }

    @Test
    @DisplayName("강연 등록 - 입력값 오류4 - 강연자명 입력하지 않음")
    fun createLectureFailTest4() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = LectureRequest(
            title = "강연 제목입니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )

        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("강연자명을 입력하지 않았습니다."))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNull(lecture)
    }

    @Test
    @DisplayName("강연 등록 - 입력값 오류5 - 강연 예약마감인원이 1보다 작은 경우")
    fun createLectureFailTest5() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = LectureRequest(
            title = "강연 제목입니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = 0,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )

        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("예약 마감인원은 1명 이상이여야 합니다."))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNull(lecture)
    }

    @Test
    @DisplayName("강연 등록 - 입력값 오류6 - 강연 시작일자가 등록일자보다 이전인 경우")
    fun createLectureFailTest6() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = LectureRequest(
            title = "강연 제목입니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().minusHours(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )

        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("강연 시작일시를 정확히 입력하세요."))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNull(lecture)
    }

    @Test
    @DisplayName("강연 등록 - 입력값 오류7 - 강연 종료일자가 시작일자보다 이전인 경우")
    fun createLectureFailTest7() {
        val newRoom = createRoom()
        assertNotNull(newRoom)

        val lectureRequest = LectureRequest(
            title = "강연 제목입니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().plusDays(1).plusHours(2),
            closedAt = LocalDateTime.now().plusDays(1),
        )

        mockMvc.perform(post("/api/v1/rooms/${newRoom.id}/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("강연 종료일시를 정확히 입력하세요."))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(lectureRequest.title)
        assertNull(lecture)
    }

    @Test
    @DisplayName("강연 수정 - 입력값 정상")
    fun updateLectureSuccessTest() {
        val room = createRoom()
        assertNotNull(room)

        val newLecture = createLecture(room)
        assertNotNull(newLecture)

        val updateLectureRequest = LectureRequest(
            title = "강연 제목입니다. - 수정",
            description = "강연 상세내용입니다. - 수정",
            lecturerName = "강연자 - 수정",
            limitOfReservations = 21,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )

        mockMvc.perform(put("/api/v1/rooms/${room.id}/lectures/${newLecture.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateLectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value(updateLectureRequest.title))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(updateLectureRequest.description))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.lecturerName").value(updateLectureRequest.lecturerName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.limitOfReservations").value(updateLectureRequest.limitOfReservations))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.openedAt").value(updateLectureRequest.openedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.closedAt").value(updateLectureRequest.closedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(updateLectureRequest.title)
        assertNotNull(lecture)
        assertEquals(updateLectureRequest.title, lecture?.title)
        assertEquals(updateLectureRequest.description, lecture?.description)
        assertEquals(updateLectureRequest.lecturerName, lecture?.lecturerName)
        assertEquals(updateLectureRequest.limitOfReservations, lecture?.limitOfReservations)
        assertEquals(
            updateLectureRequest.openedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            lecture?.openedAt?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        assertEquals(
            updateLectureRequest.closedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            lecture?.closedAt?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
    }

    @Test
    @DisplayName("강연 수정 - 입력값 오류1 - 강연 마감인원은 현재 신청인원보다 커야한다.")
    fun updateLectureFailTest1() {
        val room = createRoom()
        val newLecture = createLecture(room)

        val updateLectureRequest = LectureRequest(
            title = "강연 제목입니다. - 수정",
            description = "강연 상세내용입니다. - 수정",
            lecturerName = "강연자 - 수정",
            limitOfReservations = 21,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )

        mockMvc.perform(put("/api/v1/rooms/${room.id}/lectures/${newLecture.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateLectureRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value(updateLectureRequest.title))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(updateLectureRequest.description))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.lecturerName").value(updateLectureRequest.lecturerName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.limitOfReservations").value(updateLectureRequest.limitOfReservations))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.openedAt").value(updateLectureRequest.openedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.closedAt").value(updateLectureRequest.closedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andDo(MockMvcResultHandlers.print())

        val lecture = lectureRepository.findByTitle(updateLectureRequest.title)
        assertNotNull(lecture)
        assertEquals(updateLectureRequest.title, lecture?.title)
        assertEquals(updateLectureRequest.description, lecture?.description)
        assertEquals(updateLectureRequest.lecturerName, lecture?.lecturerName)
        assertEquals(updateLectureRequest.limitOfReservations, lecture?.limitOfReservations)
        assertEquals(
            updateLectureRequest.openedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            lecture?.openedAt?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        assertEquals(
            updateLectureRequest.closedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            lecture?.closedAt?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
    }


    private fun createRoom(): Room {
        val room = Room(
            title = "테스트 강연장",
            limitOfPersons = 20,
            status = CommonStatus.ACTIVE,
            createdBy = "94042",
        )
        return roomRepository.save(room)
    }

    private fun createLecture(room: Room): Lecture {
        val lecture = Lecture(
            room = room,
            title = "강연 제목입니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
            createdBy = "94042"
        )
        return lectureRepository.save(lecture)
    }

    private fun createLectureRequest(): LectureRequest {
        return LectureRequest(
            title = "강연 제목입니다.",
            description = "강연 상세내용입니다.",
            lecturerName = "강연자",
            limitOfReservations = 20,
            openedAt = LocalDateTime.now().plusDays(1),
            closedAt = LocalDateTime.now().plusDays(1).plusHours(2),
        )
    }
}