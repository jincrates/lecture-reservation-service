package me.jincrates.lecturereservationservice.exception

import java.lang.RuntimeException

/**
 * RuntimeException을 상속받는 ServerException 생성
 */
sealed class ServerException(
    val code: Int,
    override val message: String
) : RuntimeException(message)

data class NotFoundException(
    override val message: String
) : ServerException(404, message)
