package com.impuls.billing.side_billing.model.out_dto.dto

data class PromisePaymentDto(
    val recordId: Long,
    val agrmId: Long,
    val payId: Long,
    val currId: Long,
    val debt: Double,
    val amount: Double,
    val promiseDate: String,
    val promiseTill: String,
    val symbol: String,
    val username: String,
    val agrmNum: String,
    val status: Int,
    val modPerson: Long,
    val cancelPerson: Long,
    val modFio: String,
    val cancelFio: String,
    val fee: Double,
)
