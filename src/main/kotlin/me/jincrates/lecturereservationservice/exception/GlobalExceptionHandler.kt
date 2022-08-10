package me.jincrates.lecturereservationservice.exception

import mu.KotlinLogging
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

/**
 * 공통 에러처리 - 전체 컨트롤러 예외사항을 감지합니다.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    /**
     * 런타임 오류에 대한 에러처리
     */
    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException) : ErrorResponse {
        logger.error { ex.message }  //에러 로깅
        return ErrorResponse(code = ex.code, message = ex.message)
    }

    /**
     * 필드 유효성 에러처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException) : ErrorResponse {
        val errorMessage = ex.bindingResult.allErrors[0].defaultMessage.toString()
        logger.error { errorMessage }  //에러 로깅
        return ErrorResponse(code = 400, message = errorMessage)
    }

    /**
     * ServerException에 정의되지 않은 예외처리
     */
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception) : ErrorResponse {
        logger.error { ex.message }
        // 보안취약점: 서버 오류가 클라이언트에게 노출되지 않도록 설정(logging을 통해서만 확인 가능)
        return ErrorResponse(code = 500, message = "Internal Server Error")
    }
}