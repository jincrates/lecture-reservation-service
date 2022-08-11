package me.jincrates.lecturereservationservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import me.jincrates.lecturereservationservice.domain.Lecture
import me.jincrates.lecturereservationservice.domain.Reservation
import me.jincrates.lecturereservationservice.domain.annotation.StringFormatDateTime
import org.hibernate.validator.constraints.Length
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class LectureRequest(

    @field: NotBlank(message = "강연 제목을 입력하지 않았습니다.")
    @field: Length(max = 50, message = "강연 제목은 50자까지만 입력할 수 있습니다.")
    val title: String,

    @field: NotBlank(message = "강연 상세내용을 입력하지 않았습니다.")
    val description: String,

    @field: NotBlank(message = "강연자명을 입력하지 않았습니다.")
    val lecturerName: String,

    @field: Min(value = 1, message = "예약 마감인원은 1명 이상이여야 합니다.")
    val limitOfReservations: Int,

    @field: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val openedAt: LocalDateTime,

    @field: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val closedAt: LocalDateTime,
)

data class LectureResponse(

    val id: Long? = null,

    val roomId: Long,

    val title: String,   //강연 제목

    val description: String,  //강연 상세내용

    val lecturerName: String,  //강연자명

    val limitOfReservations: Int,  //예약(신청)마감인원

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val openedAt: LocalDateTime,   //강연시작일시

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val closedAt: LocalDateTime,   //강연종료일시

    val reservations: List<ReservationResponse> = emptyList(),

    val createdBy: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,
)

fun Lecture.toResponse() = LectureResponse(
    id = id!!,
    roomId = room.id!!,
    title = title,
    description = description,
    lecturerName = lecturerName,
    limitOfReservations = limitOfReservations,
    openedAt = openedAt,
    closedAt = closedAt,
    reservations = reservations.sortedByDescending(Reservation::id).map(Reservation::toResponse),
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt,
)


data class LectureOnlyResponse(

    val id: Long? = null,

    val roomId: Long,

    val title: String,   //강연 제목

    val description: String,  //강연 상세내용

    val lecturerName: String,  //강연자명

    val limitOfReservations: Int,  //예약(신청)마감인원

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val openedAt: LocalDateTime,   //강연시작일시

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val closedAt: LocalDateTime,   //강연종료일시
)

fun Lecture.toResponseLectureOnly() = LectureOnlyResponse(
    id = id!!,
    roomId = room.id!!,
    title = title,
    description = description,
    lecturerName = lecturerName,
    limitOfReservations = limitOfReservations,
    openedAt = openedAt,
    closedAt = closedAt,
)