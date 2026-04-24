package com.impuls.billing.side_billing.model.in_dto.client_vgroup

data class ClientVgroupFullResponse(
    /**
     * Основная информация об учётной записи
     */
    val vGroup: ClientVgroupResponse,

    /**
     * Расписание смены тарифов
     */
    val tarRasp: List<TarifsRaspResponse>,

    /**
     * Сетевые сегменты
     */
    val staff: List<StaffResponse>,

    /**
     * Транспортные адреса
     */
    val macstaff: List<MacStaffResponse>,

    /**
     * Тарифы, доступные для перехода
     */
    val tarstaff: List<TariffsStaffResponse>,

    /**
     * История временного увеличения скорости
     */
    val turboshape: List<TurboShapeResponse>,

    /**
     * Дополнительные поля учётной записи
     */
    val addons: List<VgroupAddonResponse>,

    /**
     * История блокировок
     */
    val blockrasp: List<BlockRaspResponse>,

    /**
     * Адреса учётной записи
     */
    val addresses: List<AddressBriefResponse>
)
