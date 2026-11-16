package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.out_dto.dto.*
import com.impuls.billing.side_billing.model.out_dto.request.PromisePaymentRequest
import com.impuls.billing.side_billing.repository.PaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentService: PaymentService
) {

    @GetMapping("/ppSettings")
    suspend fun getPPSettings(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrm_id", required = true) agrmId: Long,
    ): PPSettingsDto {
        return paymentService.getPPSettings(
            authHeader = authHeader,
            agrmId = agrmId
        )
    }

    @GetMapping("/recommended")
    suspend fun getRecommendedPayment(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrm_id", required = true) agrmId: Long,
        @RequestParam(name = "mode", required = false) mode: Long?,
    ): RecommendedPaymentDto {
        return paymentService.getRecommendedPayment(
            authHeader = authHeader,
            agrmId = agrmId,
            mode = mode
        )
    }

    @PostMapping("/promisePayment")
    suspend fun addClientPromisePayment(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: PromisePaymentRequest
    ): ResultSuccessDto {
        return paymentService.addClientPromisePayment(
            authHeader = authHeader,
            agreementId = body.agreementId,
            sum = body.sum,
        )
    }

    @GetMapping("/promisePayment")
    suspend fun getClientPromisePayments(
        @RequestHeader("Authorization") authHeader: String,
    ): List<PromisePaymentDto> {
        return paymentService.getClientPromisePayments(
            authHeader = authHeader
        )
    }

    @GetMapping("/payment")
    suspend fun getClientPayments(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrmnum", defaultValue = "", required = false) agrmNum: String,
        @RequestParam(name = "dtto", defaultValue = "", required = false) dateTo: String,
        @RequestParam(name = "dtfrom", defaultValue = "", required = false) dateFrom: String,
    ): List<ClientPayment> {
        return paymentService.getClientPayments(
            authHeader = authHeader,
            agrmNum = agrmNum,
            dateTo = dateTo,
            dateFrom = dateFrom
        )
    }

    @GetMapping("/order")
    suspend fun getClientOrders(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "payed", required = true) payed: Long,
        @RequestParam(name = "dtto", required = true) dateTo: String,
        @RequestParam(name = "dtfrom", required = true) dateFrom: String,
    ): List<OrderResponse> {
        return paymentService.getClientOrders(
            authHeader = authHeader,
            payed = payed,
            dateTo = dateTo,
            dateFrom = dateFrom
        )
    }

    @GetMapping("/debits")
    suspend fun getClientDebits(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgid", required = true) vgId: Long,
        @RequestParam(name = "agrmid", required = false) agrmIdParam: Long?,
        @RequestParam(name = "armgid", required = false) armgIdLegacy: Long?,
        @RequestParam(name = "tarifid", required = true) tarifId: Long,
        @RequestParam(name = "dtto", required = true) dateTo: String,
        @RequestParam(name = "dtfrom", required = true) dateFrom: String,
    ): List<DebitDto> {
        val effectiveAgrmId = agrmIdParam ?: armgIdLegacy ?: 0L
        return paymentService.getClientDebits(
            authHeader = authHeader,
            dateTo = dateTo,
            dateFrom = dateFrom,
            vgId = vgId,
            tarifId = tarifId,
            armgId = effectiveAgrmId
        )
    }
}