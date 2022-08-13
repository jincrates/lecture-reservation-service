package me.jincrates.reservation.web

import com.fasterxml.jackson.databind.ObjectMapper
import me.jincrates.reservation.domain.*
import me.jincrates.reservation.domain.enums.CommonStatus
import me.jincrates.reservation.domain.enums.ReservationStatus
import me.jincrates.reservation.model.ReservationRequest
import me.jincrates.reservation.service.ReservationService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.config.location=classpath:application-dev.yml"])
class ReservationControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper
    @Autowired lateinit var lectureRepository: LectureRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var reservationService: ReservationService
    @Autowired lateinit var reservationRepository: ReservationRepository

    @Test
    @DisplayName("예약 신청1 - 입력값 정상")
    fun  createReservationSuccessTest1() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "01234",
            status = "APPROVAL",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(reservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(reservationRequest.status))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(reservationRequest.userId, reservation?.userId)
        assertEquals(reservationRequest.status, reservation?.status.toString())
    }

    @Test
    @DisplayName("예약 신청2 - 사번만 입력해도 자동 승인되는지 확인")
    fun  createReservationSuccessTest2() {
        val expectedStatus = "APPROVAL"

        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "01234",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(reservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(expectedStatus))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(reservationRequest.userId, reservation?.userId)
        assertEquals(expectedStatus, reservation?.status.toString())
    }

    @Test
    @DisplayName("예약 신청3 - 강의 예약 신청인원 초과 이후 신청시 예약상태가 대기(WAITING)인지 확인")
    fun  createReservationSuccessTest3() {
        val expectedStatus = "WAITING"

        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        createReservations(newLecture)

        //강의 예약마감인원과 현재 신청자가 같은지 확인(이후 신청부터는 대기 상태)
        val findLecture = lectureRepository.findByIdOrNull(newLecture.id!!)
        println(findLecture.toString())
        assertEquals(newLecture.limitOfReservations, findLecture?.reservations?.size)

        //예약마감 이후 신청하기
        val lateReservationRequest = ReservationRequest(
            userId = "01234",
        )
        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lateReservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(lateReservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(expectedStatus))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, lateReservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(lateReservationRequest.userId, reservation?.userId)
        assertEquals(expectedStatus, reservation?.status.toString())
    }

    @Test
    @DisplayName("예약 신청 - 입력값 오류1 - 사번 공백이거나, 5자리를 지키지 않았을때")
    fun  createReservationFailTest1() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(400))
            .andExpect(jsonPath("\$.message").value("사번이 잘못되었습니다. 사번은 5자리입니다."))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNull(reservation)
    }

    @Test
    @DisplayName("예약 신청 - 입력값 오류2 - 동일한 강의 중복 예약신청")
    fun  createReservationFailTest2() {
        val duplicateUserId = "01234"

        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservation = Reservation(
            lecture = newLecture,
            userId = duplicateUserId,
            status = ReservationStatus.APPROVAL,
        )
        reservationRepository.save(reservation)

        //이미 신청한 강의에 중복 예약신청
        val reservationRequest = ReservationRequest(
            userId = duplicateUserId,
        )
        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(400))
            .andExpect(jsonPath("\$.message").value("이미 신청하신 강연입니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("예약 수정 - 상태값 변경1")
    fun  updateReservationSuccessTest1() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "01234",
            status = "APPROVAL",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(reservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(reservationRequest.status))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(reservationRequest.userId, reservation?.userId)
        assertEquals(reservationRequest.status, reservation?.status.toString())
    }

    //TODO
    @Test
    @DisplayName("예약 수정 - 상태값 변경2 - 예약 승인")
    fun  updateReservationSuccessTest2() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "01234",
            status = "APPROVAL",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(reservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(reservationRequest.status))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(reservationRequest.userId, reservation?.userId)
        assertEquals(reservationRequest.status, reservation?.status.toString())
    }

    //TODO
    @Test
    @DisplayName("예약 수정 - 상태값 변경3 - 예약 대기")
    fun  updateReservationSuccessTest3() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "01234",
            status = "APPROVAL",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(reservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(reservationRequest.status))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(reservationRequest.userId, reservation?.userId)
        assertEquals(reservationRequest.status, reservation?.status.toString())
    }

    //TODO
    @Test
    @DisplayName("예약 수정 - 상태값 변경4 - 예약 취소")
    fun  updateReservationSuccessTest4() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "01234",
            status = "APPROVAL",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(reservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(reservationRequest.status))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(reservationRequest.userId, reservation?.userId)
        assertEquals(reservationRequest.status, reservation?.status.toString())
    }

    //TODO
    @Test
    @DisplayName("예약 수정 - 상태값 변경5 - 예약 취소시 다음 순번 예약자 상태가 대기(WAITING)에서 승인(APPROVAL)으로 자동 변경되는지 확인")
    fun updateReservationSuccessTest5() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val reservationRequest = ReservationRequest(
            userId = "01234",
            status = "APPROVAL",
        )

        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newLecture.id))
            .andExpect(jsonPath("\$.userId").value(reservationRequest.userId))
            .andExpect(jsonPath("\$.status").value(reservationRequest.status))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNotNull(reservation)
        assertEquals(reservationRequest.userId, reservation?.userId)
        assertEquals(reservationRequest.status, reservation?.status.toString())
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

    private fun createReservations(lecture: Lecture) {
        val reservations = mutableListOf<Reservation>()

        for (i in 1 .. lecture.limitOfReservations) {
            val reservation = Reservation(
                lecture = lecture,
                userId = getUserId(),
                status = ReservationStatus.APPROVAL
            )
            lecture.reservations.add(reservation)
            reservations.add(reservation)
        }
        reservationRepository.saveAll(reservations)
    }

    private fun getUserId(): String {
        //10000부터 99999까지의 랜덤 숫자 결과를 String으로 반환합니다.
        return (10000 .. 99999).random().toString()
    }
}