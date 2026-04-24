package com.impuls.billing.side_billing.model.in_dto

data class AvailableServiceResponse(
    val tarId: Long,
    val catIdx: Long,
    val isusBox: Boolean,
    val isUnique: Boolean,
    val descr: String,
    val saleDictServType: Long,
    val promoPeriod: Long,
    val uuid: String,
    val serviceType: Long,
    val price: Double,
    val includeAbove: Long,
    val rentPeriod: Long,
    val rentPeriodMonth: Long,
)
