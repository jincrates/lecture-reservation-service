package me.jincrates.reservation.web

import com.fasterxml.jackson.databind.ObjectMapper
import me.jincrates.reservation.domain.*
import me.jincrates.reservation.domain.enums.CommonStatus
import me.jincrates.reservation.domain.enums.ReservationStatus
import me.jincrates.reservation.model.ReservationRequest
import org.junit.jupiter.api.AfterEach
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.config.location=classpath:application-dev.yml"])
class ReservationControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper
    @Autowired lateinit var lectureRepository: LectureRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var reservationRepository: ReservationRepository

    @AfterEach
    fun after() = reservationRepository.deleteAll()

    @Test
    @DisplayName("?????? ??????1 - ????????? ??????")
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
    @DisplayName("?????? ??????2 - ????????? ???????????? ?????? ??????????????? ??????")
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
    @DisplayName("?????? ??????3 - ?????? ?????? ???????????? ?????? ?????? ????????? ??????????????? ??????(WAITING)?????? ??????")
    fun  createReservationSuccessTest3() {
        val expectedStatus = "WAITING"

        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        createReservations(newLecture)

        //?????? ????????????????????? ?????? ???????????? ????????? ??????(?????? ??????????????? ?????? ??????)
        val findLecture = lectureRepository.findByIdOrNull(newLecture.id!!)
        println(findLecture.toString())
        assertEquals(newLecture.limitOfReservations, findLecture?.reservations?.size)

        //???????????? ?????? ????????????
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
    @DisplayName("?????? ?????? - ????????? ??????1 - ?????? ???????????????, 5????????? ????????? ????????????")
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
            .andExpect(jsonPath("\$.message").value("????????? ?????????????????????. ????????? 5???????????????."))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, reservationRequest.userId)
        assertNull(reservation)
    }

    @Test
    @DisplayName("?????? ?????? - ????????? ??????2 - ????????? ?????? ?????? ????????????")
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

        //?????? ????????? ????????? ?????? ????????????
        val reservationRequest = ReservationRequest(
            userId = duplicateUserId,
        )
        mockMvc.perform(post("/api/v1/lectures/${newLecture.id}/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.code").value(400))
            .andExpect(jsonPath("\$.message").value("?????? ???????????? ???????????????."))
            .andDo(print())
    }

    @Test
    @DisplayName("?????? ?????? - ????????? ??????1")
    fun  updateReservationSuccessTest1() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val newReservation = Reservation(
            userId = "01234",
            lecture = newLecture,
            status = ReservationStatus.WAITING,
        )
        val savedReservation = reservationRepository.save(newReservation)

        val modReservationRequest = ReservationRequest(
            userId = "01234",
            status = "CANCEL",
        )
        mockMvc.perform(put("/api/v1/lectures/${newLecture.id}/reservations/${savedReservation.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(modReservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(savedReservation.lecture.id))
            .andExpect(jsonPath("\$.userId").value(savedReservation.userId))
            .andExpect(jsonPath("\$.status").value(savedReservation.status.toString()))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, savedReservation.userId)
        assertNotNull(reservation)
        assertEquals(savedReservation.userId, reservation?.userId)
        assertEquals(savedReservation.status, reservation?.status)
    }

    @Test
    @DisplayName("?????? ?????? - ????????? ??????2 - ?????? ??????")
    fun  updateReservationSuccessTest2() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val newReservation = Reservation(
            userId = "01234",
            lecture = newLecture,
            status = ReservationStatus.WAITING,
        )
        val savedReservation = reservationRepository.save(newReservation)

        val modReservationRequest = ReservationRequest(userId = "01234")
        mockMvc.perform(put("/api/v1/lectures/${newLecture.id}/reservations/${savedReservation.id}/approval")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(modReservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(savedReservation.lecture.id))
            .andExpect(jsonPath("\$.userId").value(savedReservation.userId))
            .andExpect(jsonPath("\$.status").value("APPROVAL"))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, savedReservation.userId)
        assertNotNull(reservation)
        assertEquals(savedReservation.userId, reservation?.userId)
        assertEquals(ReservationStatus.APPROVAL, reservation?.status)
    }

    @Test
    @DisplayName("?????? ?????? - ????????? ??????3 - ?????? ??????")
    fun  updateReservationSuccessTest3() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val newReservation = Reservation(
            userId = "01234",
            lecture = newLecture,
            status = ReservationStatus.APPROVAL,
        )
        reservationRepository.save(newReservation)

        val modReservationRequest = ReservationRequest(userId = "01234")
        mockMvc.perform(put("/api/v1/lectures/${newLecture.id}/reservations/${newReservation.id}/waiting")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(modReservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newReservation.lecture.id))
            .andExpect(jsonPath("\$.userId").value(newReservation.userId))
            .andExpect(jsonPath("\$.status").value("WAITING"))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, newReservation.userId)
        assertNotNull(reservation)
        assertEquals(newReservation.userId, reservation?.userId)
        assertEquals(ReservationStatus.WAITING, reservation?.status)
    }

    @Test
    @DisplayName("?????? ?????? - ????????? ??????4 - ?????? ??????")
    fun  updateReservationSuccessTest4() {
        val newRoom = createRoom()
        val newLecture = createLecture(newRoom)
        val newReservation = Reservation(
            userId = "01234",
            lecture = newLecture,
            status = ReservationStatus.APPROVAL,
        )
        reservationRepository.save(newReservation)

        val modReservationRequest = ReservationRequest(userId = "01234")
        mockMvc.perform(put("/api/v1/lectures/${newLecture.id}/reservations/${newReservation.id}/cancel")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(modReservationRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.lectureId").value(newReservation.lecture.id))
            .andExpect(jsonPath("\$.userId").value(newReservation.userId))
            .andExpect(jsonPath("\$.status").value("CANCEL"))
            .andDo(print())

        val reservation = reservationRepository.findByLectureIdAndUserId(newLecture.id, newReservation.userId)
        assertNotNull(reservation)
        assertEquals(newReservation.userId, reservation?.userId)
        assertEquals(ReservationStatus.CANCEL, reservation?.status)
    }

    @Test
    @DisplayName("?????? ??????")
    fun deleteReservation() {
        val room = createRoom()
        val lecture = createLecture(room)
        val reservation = createReservation(lecture)

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/v1/lectures/${lecture.id}/reservations/${reservation.id}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andDo(print())

        val existReservation = reservationRepository.existsById(reservation.id!!)
        assertFalse(existReservation)
    }

    private fun createReservation(lecture: Lecture): Reservation {
        val reservation = Reservation(
            userId = "01234",
            lecture = lecture,
            status = ReservationStatus.APPROVAL,
        )
        return reservationRepository.save(reservation)
    }

    private fun createRoom(): Room {
        val room = Room(
            title = "????????? ?????????",
            limitOfPersons = 100,
            status = CommonStatus.ACTIVE,
            createdBy = "94042",
        )
        return roomRepository.save(room)
    }

    private fun createLecture(room: Room): Lecture {
        val lecture = Lecture(
            room = room,
            title = "?????? ???????????????.",
            description = "?????? ?????????????????????.",
            lecturerName = "?????????",
            limitOfReservations = 100,
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
        //10000?????? 99999????????? ?????? ?????? ????????? String?????? ???????????????.
        return (10000 .. 99999).random().toString()
    }
}