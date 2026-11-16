package com.impuls.billing.side_billing.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BillingException::class)
    fun handleBillingException(ex: BillingException): ResponseEntity<MutableMap<String, String>> {
        log.warn("BillingException [{}]: {}", ex.status, ex.message)
        val error: MutableMap<String, String> = HashMap()
        error["message"] = ex.localizedMessage ?: "Billing error"

        return ResponseEntity.status(ex.status).body(error)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<MutableMap<String, String>> {
        log.error("Unhandled exception: ", ex)
        val error: MutableMap<String, String> = HashMap()
        error["message"] = ex.message ?: "An unexpected error occurred. Please try again later."

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error)
    }
}
