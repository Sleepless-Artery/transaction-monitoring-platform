package org.sleepless_artery.transaction_api.api.error

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {

        val message = exception.bindingResult
            .fieldErrors
            .joinToString("; ") {
                "${it.field}: ${it.defaultMessage ?: "invalid value"}"
            }

        return error(
            status = HttpStatus.BAD_REQUEST,
            message = message,
            path = request.requestURI
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        exception: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> =
        error(
            status = HttpStatus.BAD_REQUEST,
            message = exception.message ?: "Invalid request",
            path = request.requestURI
        )

    private fun error(
        status: HttpStatus,
        message: String,
        path: String
    ): ResponseEntity<ApiErrorResponse> =
        ResponseEntity
            .status(status)
            .body(
                ApiErrorResponse(
                    timestamp = Instant.now(),
                    status = status.value(),
                    error = status.reasonPhrase,
                    message = message,
                    path = path
                )
            )
}