package com.impuls.billing.side_billing.model.in_dto.tariff

data class TariffsSettingsRentAsServiceResponse(
    val includeAbove: Double,//		Стоимость подключения
    val rentPeriod: Int,//			Периодичность списания услуги
    val rentPeriodMonth: Int,//		Периодичность списания, если настроены списания раз в N месяцев
    val beginPeriod: Int,//		Расчётный период
    val rent: Double,//			Величина аренды в валюте тарифа
    val blockRent: Double,//			Величина аренды при блокировке (по балансу) в валюте тарифа
    val usrBlockRent: Double,//			Величина аренды при блокировке (пользовательской) в валюте тарифа
    val admBlockRent: Double,//			Величина аренды при блокировке (административной) в валюте тарифа
)
