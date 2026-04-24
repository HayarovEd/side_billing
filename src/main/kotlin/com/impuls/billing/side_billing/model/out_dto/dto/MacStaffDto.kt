package com.impuls.billing.side_billing.model.out_dto.dto

data class MacStaffDto(
    val macId: Long,
    val recordId: Long,
    val vgId: Long,
    val segment: String,
    val mac: String
)
