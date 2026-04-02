package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.out_dto.dto.PromisePaymentDto
import com.impuls.billing.side_billing.model.out_dto.request.PromisePaymentRequest
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.repository.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentService: PaymentService
) {

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
    ): Mono<PromisePaymentDto> {
        return paymentService.getClientPromisePayments(
            authHeader = authHeader
        )
    }
}