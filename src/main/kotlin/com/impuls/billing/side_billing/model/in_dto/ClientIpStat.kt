package com.impuls.billing.side_billing.model.in_dto

data class ClientIpStat(
    val agrmId: Long?,
    val vgId: Long?,
    val agentId: Long?,
    val agentType: Long?,
    val agentDescr: String?,
    val currSymbol: String?,
    val srcIp: String?,
    val volume: Double?,
    val volumeIn: Double?,
    val volumeOut: Double?,
    val amount: Double?
)
