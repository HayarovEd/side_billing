package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.model.entity.Token
import com.impuls.billing.side_billing.service.AuthRepository
import com.impuls.billing.side_billing.utils.BEARER_PREF
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val authRepository: AuthRepository,
) {
    fun getSession(authHeader: String): Token? {
        val token = authHeader.removePrefix(BEARER_PREF)
            .trim()
        return authRepository.findByToken(token)
    }
}