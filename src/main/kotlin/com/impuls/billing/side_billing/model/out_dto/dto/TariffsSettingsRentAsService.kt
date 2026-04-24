package com.impuls.billing.side_billing.model.out_dto.dto

data class TariffsSettingsRentAsService(
    val includeAbove: Double,
    val rentPeriod: Int,
    val rentPeriodMonth: Int,
    val beginPeriod: Int,
    val rent: Double,
    val blockRent: Double,
    val usrBlockRent: Double,
    val admBlockRent: Double,
)
