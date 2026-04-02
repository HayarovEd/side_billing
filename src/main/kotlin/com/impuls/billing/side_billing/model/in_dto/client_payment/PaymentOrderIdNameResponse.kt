package com.impuls.billing.side_billing.model.in_dto.client_payment

data class PaymentOrderIdNameResponse(
    val orderId: Long, //		Идентификатор счета	2.0.48
    val orderNum: String, //		Номер счета	2.0.48
)
