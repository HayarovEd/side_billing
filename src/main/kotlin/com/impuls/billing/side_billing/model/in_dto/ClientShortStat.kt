package com.impuls.billing.side_billing.model.in_dto

data class ClientShortStat(
    val agrmId: Long?,
    val vgId: Long?,
    val agentId: Long?,
    val agentType: Long?,
    val contIn: Long?,
    val cntOut: Long?,
    val duration: Long?,
    val amount: Double?,
    val dt: String?,
    val agentDescr: String?,
    val currSymbol: String?,
    val volume: Double?,
    val volumeIn: Double?,
    val volumeOut: Double?,
    val total: Long?
)
