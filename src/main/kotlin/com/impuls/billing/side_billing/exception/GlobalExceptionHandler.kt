package com.impuls.billing.side_billing.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(MeteoException::class)
    fun handleMeteoException(ex: MeteoException): ResponseEntity<MutableMap<String, String>> {
        val error: MutableMap<String, String> = HashMap()
        error["message"] = ex.localizedMessage.toString()

        return ResponseEntity.status(ex.status).body(error)
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<MutableMap<String, String>> {
        val error: MutableMap<String, String> = HashMap()
        error["message"] = "An unexpected error occurred. Please try again later."

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error)
    }
}
