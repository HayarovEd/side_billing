package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.InternalBillingService
import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AccountService(
    private val internalBillingService: InternalBillingService,
    private val tokenService: TokenService,
) {

    fun getAccountData(authHeader: String): Mono<ClientAccount> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({ internalBillingService.getClientAccount(session.sessionId) })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }
}