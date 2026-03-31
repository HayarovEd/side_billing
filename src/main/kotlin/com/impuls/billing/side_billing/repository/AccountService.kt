package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.InternalBillingService
import com.impuls.billing.side_billing.model.out_dto.ClientAccount
import com.impuls.billing.side_billing.security.BEARER_PREF
import com.impuls.billing.side_billing.service.AuthRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AccountService(
    private val internalBillingService: InternalBillingService,
    private val authRepository: AuthRepository,
) {

    fun getAccountData(authHeader: String): Mono<ClientAccount> {
        val token = authHeader.removePrefix(BEARER_PREF)
            .trim()
        val session = authRepository.findByToken(token)
        return session?.let {
            val result = internalBillingService.getClientAccount(session.sessionId)
            result.map { rs ->
                when (rs) {
                    null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                    else -> {
                        rs
                    }
                }
            }
        }?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }
}