package com.impuls.billing.side_billing.model.in_dto.usbox_service

data class TariffModifierResponse(
    /**
     * Идентификатор записи в БД
     */
    val recordId: Long,

    /**
     * Идентификатор тарифного плана
     */
    val tarId: Long,

    /**
     * Целочисленный идентификатор услуги в БД
     */
    val servId: Long,

    /**
     * Целочисленный идентификатор дополнительной услуги в БД
     */
    val serviceId: Long,

    /**
     * Идентификатор учётной записи
     */
    val vgId: Long,

    /**
     * Флаг «переносить скидку на абонентскую плату»
     */
    val keepModifier: Long,

    /**
     * Идентификатор пакета услуг, по которому назначена скидка
     */
    val userPacketId: Long,

    /**
     * Идентификатор маркетинговой акции, по которой назначена скидка
     */
    val actionStaffId: Long,

    /**
     * Идентификатор расписания смены тарифного плана
     */
    val tarifsRaspId: Long,

    /**
     * Статус учётной записи:
     * * 0 — активна
     * * 1 — заблокирована по балансу
     * * 2 — заблокирована пользователем
     * * 3 — заблокирована администратором
     * * 4 — заблокирована по балансу
     * * 10 — отключена
     */
    val blockState: Long,

    /**
     * Идентификатор родительского модификатора
     */
    val parentRecordId: Long,

    /**
     * Идентификатор менеджера
     */
    val personId: Long,

    /**
     * Абонентская плата услуги
     */
    val rent: Double,

    /**
     * Абонентская плата при блокировке
     */
    val blockRent: Double,

    /**
     * Величина скидки в процентах
     */
    val rate: Double,

    /**
     * Коэффициент скидки
     */
    val discount: Double,

    /**
     * Коэффициент скидки на абонентскую плату при блокировке
     */
    val blockDiscount: Double,

    /**
     * Дата начала оказания услуги
     * Формат: YYYY-MM-DD HH24:MI:SS
     */
    val timeFrom: String,

    /**
     * Дата завершения оказания услуги
     * Формат: YYYY-MM-DD HH24:MI:SS
     */
    val timeTo: String,

    /**
     * Название услуги
     */
    val name: String
)
