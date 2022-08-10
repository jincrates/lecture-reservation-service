package me.jincrates.lecturereservationservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import me.jincrates.lecturereservationservice.domain.Lecture
import me.jincrates.lecturereservationservice.domain.Reservation
import me.jincrates.lecturereservationservice.domain.enums.ReservationStatus
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class ReservationRequest(

    @field: NotBlank(message = "신청자 사번을 입력하지 않았습니다.")
    @field: Length(min = 5, max = 5, message = "사번은 5자리입니다.")
    val userId: String,

    val status: String?,
)

data class ReservationResponse(

    val id: Long? = null,

    val lectureId: Long,

    val userId: String,

    val status: ReservationStatus,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,
)

fun Reservation.toResponse() = ReservationResponse(
    id = id!!,
    lectureId = lecture.id!!,
    userId = userId,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)