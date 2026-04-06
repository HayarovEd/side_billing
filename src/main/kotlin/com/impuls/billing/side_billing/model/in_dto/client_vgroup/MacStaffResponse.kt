package com.impuls.billing.side_billing.model.in_dto.client_vgroup

data class MacStaffResponse(
    val macId: Long,      // Идентификатор записи о назначенном MAC-адресе в таблице MAC-адресов текущих сессий
    val recordId: Long,   // Идентификатор записи в БД
    val vgId: Long,       // Идентификатор учетной записи
    val segment: String,  // IP-подсеть или автономная сеть, назначенная учетной записи
    val mac: String       // Назначенный MAC-адрес
)
