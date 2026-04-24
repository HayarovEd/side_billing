package com.impuls.billing.side_billing.model.out_dto.dto

data class UprsPayment(
    val uprsCurrency: String,//		Наименование валюты, в которой совершён платёж
    val paymentDate: String,//		Дата и время платежа на терминале. Указывается на квитанции (чеке)
    val agentAcceptedDate: String,//		Дата операции — дата формирования документа о платеже партнером ЕСПП для передачи документа в сторону ЕСПП
    val acceptedDate: String,//		Дата и время принятия платежа в ЕСПП
    val agentOperationId: Long,//		Идентификатор операции на стороне ЕСПП
    val paymentToolType: Long,//		Тип платёжного инструмента
    val paymentToolNumber: String,//		Номер платёжного инструмента
    val checkNumber: Long,//		Номер чека
    val terminalNumber: String,//		Номер терминала
    val agentSystemCode: String,//		Код ВПС
    val agentName: String,//		Название ВПС
    val subscriberId: String,//		Номер договора абонента
    val terminalId: String,//		Идентификатор терминала
    val terminalAddress: String,//		Адрес терминала
    val payerBankAccount: String,//		Расчётный счёт банка плательщика
    val payerBankName: String,//		Название банка плательщика
    val payerBik: String,//		БИК плательщика
    val payerBankAddress: String,//		Адрес банка плательщика
    val more: String,//		Сопутствующая информация о платеже
)
