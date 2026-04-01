package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.exception.BillingException
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono

fun <T : Any> monoMapper(bodyFunction: () -> Mono<T>): Mono<T> {
    return bodyFunction()
        .switchIfEmpty(Mono.error(BillingException("User not found", HttpStatus.UNAUTHORIZED)))
}