package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.HomeInternetBillingService
import com.impuls.billing.side_billing.in_controller.PaymentBillingService
import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ClientPayment
import com.impuls.billing.side_billing.model.out_dto.dto.DebitDto
import com.impuls.billing.side_billing.model.out_dto.dto.PromisePaymentDto
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.utils.convertToDate
import com.impuls.billing.side_billing.utils.monoMapper
import com.impuls.billing.side_billing.utils.parseFlexibleDate
import com.impuls.billing.side_billing.utils.splitByMonths
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.format.DateTimeFormatter

@Service
class PaymentService(
    private val tokenService: TokenService,
    private val paymentBillingService: PaymentBillingService,
    private val homeInternetBillingService: HomeInternetBillingService
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

    fun getClientDebits(
        authHeader: String,
        vgId: Long,
        armgId: Long,
        tarifId: Long,
        dateTo: String,
        dateFrom: String,
    ): Mono<List<DebitDto>> {
        val session = tokenService.getSession(authHeader)
        val start = parseFlexibleDate(dateFrom)
        println(start)
        val end = parseFlexibleDate(dateTo)
        return session?.let {
            monoMapper({
                val tariffHistory = homeInternetBillingService.getTarifsHistory(
                    sessionId = it.sessionId,
                    vgId = vgId
                )
                val rent = paymentBillingService.getRentAmount(
                sessionId = it.sessionId,
                armgId = armgId,
                tarifId = tarifId
            )
                val dateSetTariff = tariffHistory.map { tarifs ->
                    convertToDate(tarifs.first().changed)
                }
                val resultStart = dateSetTariff.map { sd ->
                    if (sd > start) sd else start
                }

                val counts = resultStart.map { rs ->
                    splitByMonths(
                        startDate = rs,
                        endDate = end,
                    )
                }
                counts.zipWith(rent) { cts, rt ->
                    cts.map { ct ->
                        DebitDto(
                            yearMonth = ct.yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01",
                            amount = ct.days * rt
                        )
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

}