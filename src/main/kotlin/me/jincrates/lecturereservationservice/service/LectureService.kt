package me.jincrates.lecturereservationservice.service

import me.jincrates.lecturereservationservice.model.LectureRequest
import me.jincrates.lecturereservationservice.model.LectureResponse
import org.springframework.stereotype.Service

@Service
class LectureService {
    fun create(userId: String, request: LectureRequest): LectureResponse {
        TODO("Not yet implemented")
    }
}