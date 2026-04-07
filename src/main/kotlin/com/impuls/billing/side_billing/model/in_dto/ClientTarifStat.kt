package com.impuls.billing.side_billing.model.in_dto

data class ClientTarifStat(
    val agrmId: Long?,
    val vgId: Long?,
    val agentId: Long?,
    val agentType: Long?,
    val agentDescr: String?,
    val zoneDescr: String?,
    val currSymbol: String?,
    val tarId: Long?,
    val catIdx: Long?,
    val volume: Double?,
    val volumeIn: Double?,
    val volumeOut: Double?,
    val amount: Double?
)
