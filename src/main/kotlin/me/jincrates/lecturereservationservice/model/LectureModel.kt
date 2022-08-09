package me.jincrates.lecturereservationservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import me.jincrates.lecturereservationservice.domain.Lecture
import me.jincrates.lecturereservationservice.domain.Reservation
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class LectureRequest(

    @field: NotBlank(message = "강연자명을 입력하지 않았습니다.")
    val lecturerName: String,

    @field: Min(1)
    val numberOfReservations: Int,

    @field: JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val openedAt: LocalDateTime,

    @field: JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val closedAt: LocalDateTime,

    @field: NotBlank(message = "강연 제목을 입력하지 않았습니다.")
    @field: Length(max = 50, message = "강연 제목은 50자까지만 입력할 수 있습니다.")
    val title: String,

    val description: String,
)

data class LectureResponse(

    val id: Long? = null,

    val roomId: Long,

    val lecturerName: String,  //강연자명

    val numberOfReservations: Int,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val openedAt: LocalDateTime,   //강연시작일시

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val closedAt: LocalDateTime,   //강연종료일시

    val title: String,   //강연 제목

    val description: String,

    val reservations: List<ReservationResponse> = emptyList(),
)

fun Lecture.toResponse() = LectureResponse(
    id = id!!,
    roomId = room.id!!,
    lecturerName = lecturerName,
    numberOfReservations = numberOfReservations,
    openedAt = openedAt,
    closedAt = closedAt,
    title = title,
    description = description,
    reservations = reservations.sortedByDescending(Reservation::id).map(Reservation::toResponse),
)