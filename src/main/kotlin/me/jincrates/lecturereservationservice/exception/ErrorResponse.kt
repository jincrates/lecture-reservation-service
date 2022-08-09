package me.jincrates.lecturereservationservice.exception

data class ErrorResponse(
    val code: Int,
    val message: String,
)