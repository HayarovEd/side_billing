package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.model.entity.Token
import com.impuls.billing.side_billing.security.JwtService
import com.impuls.billing.side_billing.utils.BEARER_PREF
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val jwtService: JwtService,
) {
    fun getSession(authHeader: String): Token? {
        val token = authHeader.removePrefix(BEARER_PREF).trim()
        if (!jwtService.validateAccessToken(token)) {
            return null
        }
        return try {
            val userId = jwtService.getUserIdFromToken(token).toLongOrNull() ?: 0L
            val sessionId = jwtService.getSessionIdFromToken(token)
            Token(
                id = userId,
                token = token,
                sessionId = sessionId
            )
        } catch (e: Exception) {
            null
        }
    }

    fun deleteSession(token: Token) {
        // Stateless JWT expire automatically
    }
}