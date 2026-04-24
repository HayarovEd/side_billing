package com.impuls.billing.side_billing.model.in_dto

data class TarifsHistoryResponse(
    val tarType: Int, //		Тип тарифа
    val recordId: Long, //		Идентификатор записи
    val vgId: Long, //		Идентификатор учётной записи, для которой был изменен тарифный план
    val agrmId: Long, //		Идентификатор договора
    val uid: Long, //		Идентификатор пользователя
    val id: Long, //		Идентификатор агента учётной записи
    val tarIdNew: Long, //		Идентификатор нового тарифного плана
    val tarIdOld: Long, //		Идентификатор старого тарифного плана
    val requestBy: String, //		Идентификатор менеджера, который инициировал смену тарифа. Если изменение инициировал абонент — NULL
    val raspTime: String, //		Запланированное время смены тарифа в формате YYYY-MM-DD HH24:MI:SS
    val changed: String, //		Фактическое время смены тарифа в формате YYYY-MM-DD HH24:MI:SS
    val vgLogin: String, //		Логин учетной записи Для сторонних биллингов: если у учётных записей нет логинов, то должен быть возвращен идентификатор.
    val agrmNum: String, //		Номер договора
    val code: String, //		Код оплаты
    val accName: String, //		Имя пользователя
    val agentName: String, //		Название агента
    val tarNewName: String, //		Название нового тарифного плана
    val tarOldName: String, //		Название старого тарифного плана
    val mgrName: String, //		ФИО менеджера или абонента, запланировавшего смену
    val mgrDescr: String, //		Краткая информация о менеджере или пользователе
    val currSymbol: String, //		Символ валюты тарифа
)
