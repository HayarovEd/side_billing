package com.impuls.billing.side_billing.model.in_dto.client_vgroup

data class IPMaskResponse(
    val ip: String,           // Назначенный IP-адрес
    val mask: Int,           // Маска подсети (unsignedInt)
    val prefix: Int,         // Длина префикса подсети (unsignedInt)
    val segmentId: Long       // Идентификатор сегмента подсети из БД
)
