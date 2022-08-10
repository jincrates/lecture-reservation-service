package me.jincrates.lecturereservationservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import me.jincrates.lecturereservationservice.domain.Lecture
import me.jincrates.lecturereservationservice.domain.Room
import me.jincrates.lecturereservationservice.domain.enums.CommonStatus
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

/**
 * RoomRequest 객체
 */
data class RoomRequest(

    @field: NotBlank(message = "강연장명을 입력하지 않았습니다.")
    @field: Length(max = 50, message = "강연장명은 50자까지만 입력할 수 있습니다.")
    val title: String,

    @field: Min(value = 1, message = "강연장 수용인원은 1명 이상입니다.")
    val limitOfPersons: Int,

    val status: CommonStatus,
)

/**
 * RoomResponse 객체
 */
data class RoomResponse(

    val id: Long,

    val title: String,

    val limitOfPersons: Int,

    val status: CommonStatus,

    val createdBy: String,

    val lectures: List<LectureResponse> = emptyList(),

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,
)

/**
 * Room 엔티티를 RoomResponse으로 변환합니다.
 */
fun Room.toResponse() = RoomResponse(
    id = id!!,
    title = title,
    limitOfPersons = limitOfPersons,
    status = status,
    createdBy = createdBy,
    lectures = lectures.sortedByDescending(Lecture::id).map(Lecture::toResponse),
    createdAt = createdAt,
    updatedAt = updatedAt
)