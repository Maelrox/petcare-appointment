package com.petcaresuite.appointment.interfaces.exception

import com.petcaresuite.appointment.application.dto.ErrorResponseDTO
import com.petcaresuite.appointment.application.service.messages.InternalErrors
import com.petcaresuite.appointment.domain.exception.AppointmentConflictException
import com.petcaresuite.appointment.domain.exception.AppointmentInvalidException
import com.petcaresuite.appointment.domain.exception.ConsultInvalidException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime

@ControllerAdvice
class RestExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(RestExceptionHandler::class.java)

    @ExceptionHandler(AppointmentConflictException::class, ConsultInvalidException::class, AppointmentInvalidException::class)
    fun handleDomainException(ex: Exception): ResponseEntity<ErrorResponseDTO> {
        val message = ex.message ?: InternalErrors.UNHANDLED_EXCEPTION
        val errorResponseDTO = ErrorResponseDTO(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = message,
            path = getRequestPath()
        )
        return ResponseEntity.status(errorResponseDTO.status).body(errorResponseDTO)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDTO> {
        val errors = ex.bindingResult.fieldErrors.map { error ->
            "${error.field}: ${error.defaultMessage}"
        }
        val errorResponse = ErrorResponseDTO(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = errors.joinToString(", "),
            path = getRequestPath()
        )
        return ResponseEntity.status(errorResponse.status).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception): ResponseEntity<ErrorResponseDTO> {
        var message = InternalErrors.UNHANDLED_EXCEPTION.format(ex.message)
        logger.error(message)
        val errorResponseDTO = ErrorResponseDTO(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = message,
            path = getRequestPath()
        )
        return ResponseEntity.status(errorResponseDTO.status).body(errorResponseDTO)
    }

    private fun getRequestPath(): String {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        return requestAttributes?.request?.requestURI ?: "/undefined"
    }

}