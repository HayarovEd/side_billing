package com.impuls.billing.side_billing.model.in_dto.tariff

data class TarifFullResponse(
    val tariff: TarifResponse,
    val settingsRentAsService: TariffsSettingsRentAsServiceResponse
)
