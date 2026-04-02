package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.PaymentBillingService
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class PaymentService(
    private val tokenService: TokenService,
    private val paymentBillingService: PaymentBillingService
) {
    @Transactional
    fun addClientPromisePayment(
        authHeader: String,
        agreementId: Long,
        sum: Double,
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = paymentBillingService.addClientPromisePayment(
                    sessionId = it.sessionId,
                    agreementId = agreementId,
                    sum = sum,
                )
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ResultSuccessDto(header == 1L)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }
}