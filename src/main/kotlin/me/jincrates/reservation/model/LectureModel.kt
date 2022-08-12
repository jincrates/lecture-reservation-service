package me.jincrates.reservation.model

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import me.jincrates.reservation.domain.Lecture
import me.jincrates.reservation.domain.Reservation
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@ApiModel(description = "강연 전송 객체입니다.")
data class LectureRequest(

    @ApiModelProperty(value = "강연 제목", required = true)
    @field: NotBlank(message = "강연 제목을 입력하지 않았습니다.")
    @field: Length(max = 50, message = "강연 제목은 50자까지만 입력할 수 있습니다.")
    val title: String,

    @ApiModelProperty(value = "강연 상세내용", required = true)
    @field: NotBlank(message = "강연 상세내용을 입력하지 않았습니다.")
    val description: String,

    @ApiModelProperty(value = "강연자명", required = true)
    @field: NotBlank(message = "강연자명을 입력하지 않았습니다.")
    val lecturerName: String,

    @ApiModelProperty(value = "예약 마감인원", required = true)
    @field: Min(value = 1, message = "예약 마감인원은 1명 이상이여야 합니다.")
    val limitOfReservations: Int,

    @ApiModelProperty(value = "강연 시작일시", required = true, example = "yyyy-MM-dd HH:mm:ss")
    @field: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val openedAt: LocalDateTime,

    @ApiModelProperty(value = "강연 종료일시", required = true, example = "yyyy-MM-dd HH:mm:ss")
    @field: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val closedAt: LocalDateTime,
)

@ApiModel(description = "강연 응답 객체입니다.")
data class LectureResponse(

    @ApiModelProperty(value = "강연 ID")
    val id: Long? = null,

    @ApiModelProperty(value = "강연장 ID")
    val roomId: Long,

    @ApiModelProperty(value = "강연 제목")
    val title: String,

    @ApiModelProperty(value = "강연 상세내용")
    val description: String,

    @ApiModelProperty(value = "강연자명")
    val lecturerName: String,

    @ApiModelProperty(value = "예약 마감인원")
    val limitOfReservations: Int,

    @ApiModelProperty(value = "강연 시작일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val openedAt: LocalDateTime,

    @ApiModelProperty(value = "강연 종료일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val closedAt: LocalDateTime,

    @ApiModelProperty(value = "예약 리스트")
    val reservations: List<ReservationResponse> = emptyList(),

    @ApiModelProperty(value = "생성자")
    val createdBy: String,

    @ApiModelProperty(value = "생성일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @ApiModelProperty(value = "수정일시", example = "yyyy-MM-dd HH:mm:ss")
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

@ApiModel(description = "강연 응답 객체(예약리스트 제거)입니다.")
data class LectureOnlyResponse(

    @ApiModelProperty(value = "강연 ID")
    val id: Long? = null,

    @ApiModelProperty(value = "강연장 ID")
    val roomId: Long,

    @ApiModelProperty(value = "강연 제목")
    val title: String,

    @ApiModelProperty(value = "강연 상세내용")
    val description: String,

    @ApiModelProperty(value = "강연자명")
    val lecturerName: String,

    @ApiModelProperty(value = "예약 마감인원")
    val limitOfReservations: Int,

    @ApiModelProperty(value = "강연 시작일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val openedAt: LocalDateTime,

    @ApiModelProperty(value = "강연 종료일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val closedAt: LocalDateTime,

    @ApiModelProperty(value = "생성자")
    val createdBy: String,

    @ApiModelProperty(value = "생성일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @ApiModelProperty(value = "수정일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,
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
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

@ApiModel(description = "강연 응답 객체(신청자 사번리스트)입니다.")
data class LectureUsersResponse(

    @ApiModelProperty(value = "강연 ID")
    val id: Long? = null,

    @ApiModelProperty(value = "강연장 ID")
    val roomId: Long,

    @ApiModelProperty(value = "강연 제목")
    val title: String,

    @ApiModelProperty(value = "강연 상세내용")
    val description: String,

    @ApiModelProperty(value = "강연자명")
    val lecturerName: String,

    @ApiModelProperty(value = "예약 마감인원")
    val limitOfReservations: Int,

    @ApiModelProperty(value = "강연 시작일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val openedAt: LocalDateTime,

    @ApiModelProperty(value = "강연 종료일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val closedAt: LocalDateTime,

    @ApiModelProperty(value = "신청자 사번리스트")
    val users: List<String> = emptyList(),

    @ApiModelProperty(value = "생성자")
    val createdBy: String,

    @ApiModelProperty(value = "생성일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @ApiModelProperty(value = "수정일시", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,
)

fun Lecture.toResponseUsers() = LectureUsersResponse(
    id = id!!,
    roomId = room.id!!,
    title = title,
    description = description,
    lecturerName = lecturerName,
    limitOfReservations = limitOfReservations,
    openedAt = openedAt,
    closedAt = closedAt,
    users = reservations.sortedBy(Reservation::userId).map(Reservation::userId),
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt,
)