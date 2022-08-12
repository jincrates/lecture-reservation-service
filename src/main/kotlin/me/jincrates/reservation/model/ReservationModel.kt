package me.jincrates.reservation.model

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.jincrates.reservation.domain.Reservation
import me.jincrates.reservation.domain.enums.ReservationStatus
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@ApiModel(description = "예약 전송 객체입니다.")
data class ReservationRequest(

    @ApiModelProperty(value = "예약자 사번", required = true)
    @field: NotBlank(message = "신청자 사번을 입력하지 않았습니다.")
    @field: Length(min = 5, max = 5, message = "사번은 5자리입니다.")
    val userId: String,

    @ApiModelProperty(value = "예약 상태")
    val status: String?,
)

@ApiModel(description = "예약 응답 객체입니다.")
data class ReservationResponse(

    @ApiModelProperty(value = "예약 ID")
    val id: Long? = null,

    @ApiModelProperty(value = "강의 ID")
    val lectureId: Long,

    @ApiModelProperty(value = "예약자 사번")
    val userId: String,

    @ApiModelProperty(value = "예약 상태")
    val status: ReservationStatus,

    @ApiModelProperty(value = "예약일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @ApiModelProperty(value = "예약수정일시", example = "yyyy-MM-dd HH:mm:ss")
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

@ApiModel(description = "예약 응답 객체(강의 리스트 포함)입니다.")
data class ReservationDetailResponse(

    @ApiModelProperty(value = "예약 ID")
    val id: Long? = null,

    @ApiModelProperty(value = "예약자 사번")
    val userId: String,

    @ApiModelProperty(value = "예약 상태")
    val status: ReservationStatus,

    @ApiModelProperty(value = "예약일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @ApiModelProperty(value = "예약수정일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,

    @ApiModelProperty(value = "강의 리스트")
    val lecture: LectureOnlyResponse,
)

fun Reservation.toDetailResponse() = ReservationDetailResponse(
    id = id !!,
    userId = userId,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lecture = lecture.toResponseLectureOnly(),
)