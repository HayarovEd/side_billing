package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.out_dto.dto.*
import com.impuls.billing.side_billing.soap.LanBillingClient
import com.impuls.billing.side_billing.utils.*
import com.impuls.billing.soap.generated.SoapFilter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class PaymentService(
    private val tokenService: TokenService,
    private val billingClient: LanBillingClient,
) {

    private fun getSessionOrThrow(authHeader: String): String {
        return tokenService.getSession(authHeader)?.sessionId
            ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    suspend fun getPPSettings(
        authHeader: String,
        agrmId: Long
    ): PPSettingsDto {
        val sessionId = getSessionOrThrow(authHeader)
        val list = billingClient.execute(sessionId) {
            getClientPPSettings(agrmId, "")
        }
        val first = list.firstOrNull()
            ?: throw BillingException("PP settings not found", HttpStatus.NOT_FOUND)
        return first.toPPSettings()
    }

    suspend fun getRecommendedPayment(
        authHeader: String,
        agrmId: Long,
        mode: Long?,
    ): RecommendedPaymentDto {
        val sessionId = getSessionOrThrow(authHeader)
        val amount = billingClient.execute(sessionId) {
            getRecommendedPayment(agrmId, mode ?: 0L, "", false)
        }
        return RecommendedPaymentDto(amount = amount)
    }

    suspend fun addClientPromisePayment(
        authHeader: String,
        agreementId: Long,
        sum: Double,
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            clientPromisePayment(agreementId, sum)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun getClientPromisePayments(
        authHeader: String
    ): List<PromisePaymentDto> {
        val sessionId = getSessionOrThrow(authHeader)
        val list = billingClient.execute(sessionId) {
            getClientPromisePayments(SoapFilter())
        }
        return list.map { it.toPromisePaymentDto() }
    }

    suspend fun getClientPayments(
        authHeader: String,
        agrmNum: String,
        dateTo: String,
        dateFrom: String,
    ): List<ClientPayment> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            if (agrmNum.isNotBlank()) this.agrmnum = agrmNum
            if (dateFrom.isNotBlank()) this.dtfrom = dateFrom
            if (dateTo.isNotBlank()) this.dtto = dateTo
        }
        val list = billingClient.execute(sessionId) {
            getClientPayments(flt, emptyList())
        }
        return list.map { it.toClientPayment() }
    }

    suspend fun getClientOrders(
        authHeader: String,
        payed: Long,
        dateTo: String,
        dateFrom: String,
    ): List<OrderResponse> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            this.payable = payed
            if (dateFrom.isNotBlank()) this.dtfrom = dateFrom
            if (dateTo.isNotBlank()) this.dtto = dateTo
        }
        val list = billingClient.execute(sessionId) {
            getClientOrders(flt, emptyList())
        }
        return list.map { it.toOrderResponse() }
    }

    suspend fun getClientDebits(
        authHeader: String,
        vgId: Long,
        armgId: Long,
        tarifId: Long,
        dateTo: String,
        dateFrom: String,
    ): List<DebitDto> {
        val sessionId = getSessionOrThrow(authHeader)
        val start = parseFlexibleDate(dateFrom)
        val end = parseFlexibleDate(dateTo)

        val flt = SoapFilter().apply { this.vgid = vgId }
        val tariffHistory = billingClient.execute(sessionId) {
            getTarifsHistory(flt)
        }

        val effectiveStart = if (tariffHistory.isNotEmpty()) {
            val dateSetTariff = convertToDate(tariffHistory.first().changed ?: "")
            if (dateSetTariff > start) dateSetTariff else start
        } else {
            start
        }

        val counts = splitIntervalByMonths(start = effectiveStart, end = end)
        val resultList = mutableListOf<DebitDto>()

        for (item in counts) {
            val monthDateStr = "${item.yearMonth.year}-${item.yearMonth.month.value}-01"
            val rent = billingClient.execute(sessionId) {
                getRentAmount(vgId, tarifId, monthDateStr)
            }
            resultList.add(
                DebitDto(
                    yearMonth = item.yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01",
                    amount = item.days * rent
                )
            )
        }

        return resultList
    }
}