package com.impuls.billing.side_billing.model.in_dto.usbox_service

import com.impuls.billing.side_billing.model.in_dto.client_vgroup.CurrentModifierResponse

data class UsboxServiceFull(
    /**
     * Общая информация об услуге
     */
    val service: UsboxServiceResponse,

    /**
     * Флаг «Служебная услуга»
     */
    val technicalService: Short,

    /**
     * Связь с IP-адресом:
     * * 0 — не связана
     * * 1 — связана с действующим IP-адресом
     * * 2 — IP-адрес действовал в прошлом
     */
    val isAssignedIp: Long,

    /**
     * Связь с номером телефона:
     * * 0 — не связана
     * * 1 — связана с действующим номером
     * * 2 — номер действовал в прошлом
     */
    val isAssignedPhone: Long,

    /**
     * Устаревший параметр. Необходимо всегда передавать значение 1
     */
    val used: Long,

    /**
     * Идентификатор договора
     */
    val agrmId: Long,

    /**
     * Идентификатор пользователя
     */
    val uid: Long,

    /**
     * Тип услуги:
     * * 0 — разовая
     * * 1 — периодическая с ежемесячным списанием
     * * 2 — ежедневная
     * * 3 — ежедневная равными долями
     * * 4 — раз в N месяцев
     */
    val common: Long,

    /**
     * Идентификатор смарт-карты
     */
    val smartCardId: Long,

    /**
     * Возможность назначать/отменять услугу из личного кабинета абонента:
     * * 0 — нет возможности
     * * 1 — есть возможность
     */
    val available: Long,

    /**
     * Возможность назначать/отменять услугу из административного интерфейса:
     * * 0 — нет возможности
     * * 1 — есть возможность
     */
    val availableForManager: Long,

    /**
     * Флаг «Назначать автоматически»:
     * * 0 — нет
     * * 1 — подключается автоматически при назначении тарифного плана
     */
    val autoAssign: Long,

    /**
     * Идентификатор каталога
     */
    val serviceCatId: Long,

    /**
     * Флаг «Уникальная услуга»
     */
    val isUnique: Long,

    /**
     * Стоимость услуги
     */
    val catAbove: Double,

    /**
     * Стоимость подключения услуги
     */
    val includeAbove: Double,

    /**
     * Стоимость с учётом скидки
     */
    val totalAmount: Double,

    /**
     * Номер договора
     */
    val agrmNumber: String,

    /**
     * Имя пользователя (ФИО / Название организации)
     */
    val username: String,

    /**
     * Логин учётной записи
     */
    val vgLogin: String,

    /**
     * Описание тарифа
     */
    val tarDescr: String,

    /**
     * Описание категории
     */
    val catDescr: String,

    /**
     * ФИО менеджера, назначившего услугу
     */
    val personName: String,

    /**
     * Серийный номер смарт-карты
     */
    val smartCardSerial: String,

    /**
     * Устаревший параметр. Время запланированного подключения учётной записи
     */
    val acconDate: String,

    /**
     * Валюта тарифа
     */
    val symbol: String,

    /**
     * Внешний идентификатор услуги
     */
    val uuid: String,

    /**
     * Информация о текущей скидке
     */
    val currentModifier: CurrentModifierResponse,

    /**
     * Тип тарифа
     */
    val tarType: Short,

    /**
     * Флаг «Услуга связана с обещанным платежом»
     */
    val isAssignedPromisePayment: Boolean
)
