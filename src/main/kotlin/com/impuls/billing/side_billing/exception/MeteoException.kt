package com.impuls.billing.side_billing.exception

import org.springframework.http.HttpStatus


class MeteoException(
    message: String,
    val status: HttpStatus
) : RuntimeException(message)


