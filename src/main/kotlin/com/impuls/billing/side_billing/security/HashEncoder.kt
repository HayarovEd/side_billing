package com.impuls.billing.side_billing.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    fun encode(password: String): String? = bcrypt.encode(password)

    fun decode(password: String, hashed: String): Boolean = bcrypt.matches(password, hashed)
}