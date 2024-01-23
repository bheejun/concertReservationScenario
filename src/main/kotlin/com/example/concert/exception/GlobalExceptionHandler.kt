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

    @ExceptionHandler(MemberNameAlreadyExistsException::class)
    fun handleMemberNameAlreadyExistsException (ex : MemberNameAlreadyExistsException,
                                                request: HttpServletRequest) : ResponseEntity<ErrorResponse>{
        logger.info("Name duplication occurred")
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex : MemberNameAlreadyExistsException,
                                               request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        logger.info("The requested resource cannot found")
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = ex.message,
            path = request.servletPath
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }



}