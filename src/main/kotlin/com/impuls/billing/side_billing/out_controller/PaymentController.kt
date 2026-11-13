package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.out_dto.dto.PPSettingsDto
import com.impuls.billing.side_billing.model.out_dto.dto.ClientPayment
import com.impuls.billing.side_billing.model.out_dto.dto.DebitDto
import com.impuls.billing.side_billing.model.out_dto.dto.PromisePaymentDto
import com.impuls.billing.side_billing.model.out_dto.dto.RecommendedPaymentDto
import com.impuls.billing.side_billing.model.out_dto.request.PromisePaymentRequest
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.repository.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentService: PaymentService
) {

    @GetMapping("/ppSettings")
    fun getPPSettings(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrm_id", required = true) agrmId: Long,
    ): Mono<PPSettingsDto> {
        return paymentService.getPPSettings(
            authHeader = authHeader,
            agrmId = agrmId
        )
    }

    @GetMapping("/recommended")
    fun getRecommendedPayment(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrm_id", required = true) agrmId: Long,
        @RequestParam(name = "mode", required = false) mode: Long?,
    ): Mono<RecommendedPaymentDto> {
        return paymentService.getRecommendedPayment(
            authHeader = authHeader,
            agrmId = agrmId,
            mode = mode
        )
    }


    @PostMapping("/promisePayment")
    fun addClientPromisePayment(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: PromisePaymentRequest
    ): Mono<ResultSuccessDto> {
        return paymentService.addClientPromisePayment(
            authHeader = authHeader,
            agreementId = body.agreementId,
            sum = body.sum,
        )
    }

    @GetMapping("/promisePayment")
    fun getClientPromisePayments(
        @RequestHeader("Authorization") authHeader: String,
    ): Mono<List<PromisePaymentDto>> {
        return paymentService.getClientPromisePayments(
            authHeader = authHeader
        )
    }

    @GetMapping("/payment")
    fun getClientPayments(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrmnum", defaultValue = "", required = false) agrmNum: String,
        @RequestParam(name = "dtto", defaultValue = "", required = false) dateTo: String,
        @RequestParam(name = "dtfrom", defaultValue = "", required = false) dateFrom: String,
    ): Mono<List<ClientPayment>> {
        return paymentService.getClientPayments(
            authHeader = authHeader,
            agrmNum = agrmNum,
            dateTo = dateTo,
            dateFrom = dateFrom
        )
    }

    @GetMapping("/order")
    fun getClientOrders(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "payed", required = true) payed: Long,
        @RequestParam(name = "dtto", required = true) dateTo: String,
        @RequestParam(name = "dtfrom", required = true) dateFrom: String,
    ): Mono<List<OrderResponse>> {
        return paymentService.getClientOrders(
            authHeader = authHeader,
            payed = payed,
            dateTo = dateTo,
            dateFrom = dateFrom
        )
    }

    @GetMapping("/debits")
    fun getClientDebits(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgid", required = true) vgId: Long,
        @RequestParam(name = "armgid", required = true) armgId: Long,
        @RequestParam(name = "tarifid", required = true) tarifId: Long,
        @RequestParam(name = "dtto", required = true) dateTo: String,
        @RequestParam(name = "dtfrom", required = true) dateFrom: String,
    ): Mono<List<DebitDto>> {
        return paymentService.getClientDebits(
            authHeader = authHeader,
            dateTo = dateTo,
            dateFrom = dateFrom,
            vgId = vgId,
            tarifId = tarifId,
            armgId = armgId
        )
    }

}