package com.impuls.billing.side_billing.model.out_dto.request

data class UsboxServiceRequest(
    val vgId: Long,
    val tarId: Long,
    val catIdx: Long,
    val mui: Double,
    val notusBox: Long = 1,
)
