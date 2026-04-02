package com.impuls.billing.side_billing.model.in_dto

data class PromisePaymentRequest(
    val agreementId: Long,
    val sum: Double,
)
