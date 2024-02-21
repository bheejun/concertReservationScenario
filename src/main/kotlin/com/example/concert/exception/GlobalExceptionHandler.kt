package com.example.concert.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(DuplicateException::class)
    fun handleDuplicate(
        ex: DuplicateException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("Duplication occurred")
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(
        ex: NotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("The requested resource cannot found")
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DoesNotMatchSecretCode::class)
    fun handleDoesNotMatchSecretCode(
        ex: DoesNotMatchSecretCode,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("Admin secret key does not match. Try again")
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AuthenticationFailureException::class)
    fun handleAuthenticationFailure(
        ex: AuthenticationFailureException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("Authentication is failed.")
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Forbidden",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(AlreadyCanceledReservationException::class)
    fun handleAlreadyCanceledReservation(
        ex: AlreadyCanceledReservationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("The Reservation is already canceled.")
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Forbidden",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(AlreadyBookedException::class)
    fun handleAlreadyBookedException(
        ex: AlreadyBookedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("Already booked.")
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(DoesNotMatchAmountException::class)
    fun doesNotMatchAmountExceptionHandler(
        ex: DoesNotMatchAmountException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("Amount for provided payment does not match with reservation's total price")
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Forbidden",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(PaymentTimeExceededException::class)
    fun paymentTimeExceededException(
        ex: PaymentTimeExceededException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.info("This Reservation is expired")
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            error = "Forbidden",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }



}




