package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.PaymentBillingService
import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ClientPayment
import com.impuls.billing.side_billing.model.out_dto.dto.PromisePaymentDto
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

    fun getClientPromisePayments(
        authHeader: String
    ): Mono<List<PromisePaymentDto>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                paymentBillingService.getClientPromisePayments(
                    sessionId = it.sessionId,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getClientPayments(
        authHeader: String,
        agrmNum: String,
        dateTo: String,
        dateFrom: String,
    ): Mono<List<ClientPayment>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                paymentBillingService.getClientPayments(
                    sessionId = it.sessionId,
                    agrmNum = agrmNum,
                    dateTo = dateTo,
                    dateFrom = dateFrom,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getClientOrders(
        authHeader: String,
        payed: Long,
        dateTo: String,
        dateFrom: String,
    ): Mono<List<OrderResponse>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                paymentBillingService.getClientOrders(
                    sessionId = it.sessionId,
                    payed = payed,
                    dateTo = dateTo,
                    dateFrom = dateFrom,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

}