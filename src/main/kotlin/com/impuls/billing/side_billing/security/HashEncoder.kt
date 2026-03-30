package com.impuls.billing.side_billing.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder(
    private val bcrypt: BCryptPasswordEncoder
) {
    fun encode(message: String): String? = bcrypt.encode(message)

    fun decode(message: String, hashed: String): Boolean = bcrypt.matches(message, hashed)
}