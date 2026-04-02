package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.exception.BillingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

fun <T : Any> monoMapper(bodyFunction: () -> Mono<T>): Mono<T> {
    return bodyFunction()
        .switchIfEmpty(Mono.error(BillingException("User not found", HttpStatus.UNAUTHORIZED)))
}


fun <T : Any> WebClient.handler(
    soapRequest: String,
    sessionId: String,
    bodyFunction: (ResponseEntity<String>) -> T?
): Mono<T> {
    return this
        .post()
        .bodyValue(soapRequest)
        .header("Set-Cookie", sessionId)
        .retrieve()
        .toEntity(String::class.java)
        .mapNotNull { response ->
            println(response)
            bodyFunction(response)
        }
        .doOnError {
            println(it)
        }
        .onErrorResume(WebClientResponseException::class.java) { ex ->
            println(ex)
            val faultString = SoapParser.parseFaultString(ex.responseBodyAsString)
            if (faultString?.contains("Invalid login", ignoreCase = true) == true) {
                // Ошибка аутентификации → 401
                Mono.error(BillingException(faultString, HttpStatus.UNAUTHORIZED))
            } else if (!faultString.isNullOrBlank()) {
                Mono.error(BillingException(faultString, HttpStatus.BAD_REQUEST))
                //E-mail for user '$1' is not confirmed
            } else {
                Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Billing service error", ex))
            }
        }
}