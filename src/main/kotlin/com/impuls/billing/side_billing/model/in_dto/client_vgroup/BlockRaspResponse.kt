package com.impuls.billing.side_billing.model.in_dto.client_vgroup

data class BlockRaspResponse(
    /**
     * Идентификатор записи
     */
    val recordId: Long,

    /**
     * Идентификатор учётной записи, для которой запланирована смена блокировки
     */
    val vgId: Long,

    /**
     * Идентификатор группы, для которой запланирована смена блокировки
     */
    val groupId: Long,

    /**
     * Идентификатор блокировки:
     * * 0 - активация
     * * 3 - административная блокировка
     * * 10 - отключение
     */
    val blkReq: Long,

    /**
     * Идентификатор агента, для учётных записей которого запланирована смена блокировки
     */
    val id: Long,

    /**
     * Не используется
     */
    val isHistory: Long,

    /**
     * Блокировка относится к добровольной
     */
    val isFreewill: Long,

    /**
     * Идентификатор менеджера, который инициировал переход в эту блокировку.
     * Если инициатор — абонент, то NULL
     */
    val requestBy: Long,

    /**
     * Идентификатор менеджера, который инициировал выход из этой блокировки
     */
    val unBlockedBy: Long,

    /**
     * Идентификатор договора
     */
    val agrmId: Long,

    /**
     * Идентификатор пользователя
     */
    val uid: Long,

    /**
     * Дата начала блокировки в формате YYYY-MM-DD HH:MM:SS
     */
    val changeTime: String,

    /**
     * Логин менеджера, запланировавшего блокировку
     */
    val mgrName: String,

    /**
     * Описание менеджера, запланировавшего блокировку
     */
    val mgrDescr: String,

    /**
     * Комментарий к блокировке
     */
    val comment: String,

    /**
     * Дата конца блокировки в формате YYYY-MM-DD HH:MM:SS
     */
    val timeTo: String,

    /**
     * Логин учётной записи
     */
    val login: String,

    /**
     * Логин менеджера, снявшего блокировку
     */
    val unBlockedManagerLogin: String,

    /**
     * ФИО менеджера, снявшего блокировку
     */
    val unBlockedManagerName: String,

    /**
     * Описание менеджера, снявшего блокировку
     */
    val unBlockedManagerDescr: String,

    /**
     * Номер договора
     */
    val agrmNum: String,

    /**
     * Имя пользователя
     */
    val username: String
)
