package com.impuls.billing.side_billing.model.out_dto.dto

data class TarifFullDto (
    val tarif: Tarif,
    val tariffsSettingsRentAsService: TariffsSettingsRentAsService
)