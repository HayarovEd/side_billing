package com.impuls.billing.side_billing.model.in_dto.client_payment

data class ClientPaymentResponse(
    val pay: PaymentResponse, //        Основные параметры платежа
    val bsoDoc: Long,//  Идентификатор БСО. Не используется, начиная с версии 2.0.39
    val timestamp: Long,//        Дата совершения платежа. Пересчёты из одной валюты в другую выполняются на эту дату
    val localtimestamp: Long,//        Дата совершения или изменения платежа по часам сервера, на котором установлен LBcore
    val amountCurr: Double, //        Сумма платежа в валюте договора
    val orderNum: String,//        Номер счёта, по которому производилась оплата. 0 — если платёж не является оплатой счета        Не используется с версии 2.0.48
   // val orders: List<PaymentOrderIdNameResponse>, //        Список оплачиваемых счетов(id-номер)    2.0.48
    val uid: Long,//        Идентификатор абонента, которому принадлежит платёж
    val operId: Long,//        Идентификатор оператора связи, который зарегистрировал платёж
    val cardNumber: Long,//        Номер карты оплаты
    val currSymb: String,//        Символ валюты платежа
    val uname: String,//        ФИО пользователя или название организации
    val agrm: String,//        Номер договора, на который зачислен платёж
    val mgr: String,//        ФИО менеджера, который провёл или исправил платёж
    val mgrDescr: String,//        Описание менеджера, который провёл или исправил платёж
    val mgrLogin: String,//        Описание менеджера, который провёл или исправил платёж
    val operName: String,//        Название оператора связи
    val login: String,//        Логин абонента
    val isEps: Boolean, //        Флаг «Платёж проведён через менеджера ВПС»
)