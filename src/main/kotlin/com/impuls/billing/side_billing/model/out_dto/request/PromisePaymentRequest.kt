package com.impuls.billing.side_billing.model.out_dto.request

data class PromisePaymentRequest(
    val agreementId: Long,
    val sum: Double,
)