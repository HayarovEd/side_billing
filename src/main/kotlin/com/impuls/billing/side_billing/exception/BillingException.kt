package com.impuls.billing.side_billing.exception

import org.springframework.http.HttpStatus


class BillingException(
    message: String,
    val status: HttpStatus
) : RuntimeException(message)


