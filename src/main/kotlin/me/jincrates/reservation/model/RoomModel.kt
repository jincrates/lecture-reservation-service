package me.jincrates.reservation.model

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import me.jincrates.reservation.domain.Lecture
import me.jincrates.reservation.domain.Room
import me.jincrates.reservation.domain.enums.CommonStatus
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@ApiModel(description = "강연장 전송 객체입니다.")
data class RoomRequest(

    @ApiModelProperty(value = "강연장명", required = true)
    @field: NotBlank(message = "강연장명을 입력하지 않았습니다.")
    @field: Length(max = 50, message = "강연장명은 50자까지만 입력할 수 있습니다.")
    val title: String,

    @ApiModelProperty(value = "강연장 수용인원", required = true)
    @field: Min(value = 1, message = "강연장 수용인원은 1명 이상입니다.")
    val limitOfPersons: Int,

    @ApiModelProperty(value = "강연장 상태", required = true)
    val status: String? = CommonStatus("ACTIVE").toString(),
)

@ApiModel(description = "강연장 응답 객체입니다.")
data class RoomResponse(

    @ApiModelProperty(value = "id")
    val id: Long,

    @ApiModelProperty(value = "강연장명")
    val title: String,

    @ApiModelProperty(value = "강연장 수용인원")
    val limitOfPersons: Int,

    @ApiModelProperty(value = "강연장 상태")
    val status: CommonStatus,

    @ApiModelProperty(value = "강연 리스트")
    val lectures: List<LectureResponse> = emptyList(),

    @ApiModelProperty(value = "생성자")
    val createdBy: String,

    @ApiModelProperty(value = "생성일시", example = "")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @ApiModelProperty(value = "수정일시")
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
    lectures = lectures.sortedByDescending(Lecture::id).map(Lecture::toResponse),
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt
)