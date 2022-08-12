package me.jincrates.reservation.exception

data class ErrorResponse(
    val code: Int,
    val message: String,
)