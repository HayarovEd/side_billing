package com.impuls.billing.side_billing.model.out_dto.dto

data class TarifsStaffDto (
    val groupId: Long,
    val groupTarId: Long,
    val groupModuleId: Long,
    val tarId: Long,
    val tarType: Long,
    val tarCurId: Long,
    val shape: Long,
    val rent: Double,
    val tarName: String,
    val tarSymbol: String,
    val tarDescrFull: String
)