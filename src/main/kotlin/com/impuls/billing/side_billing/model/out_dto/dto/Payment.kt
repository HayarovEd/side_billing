package com.impuls.billing.side_billing.model.out_dto.dto

import com.impuls.billing.side_billing.model.in_dto.client_payment.UprsPaymentResponse

data class Payment(
    val recordId: Long,//		Идентификатор платежа в БД
    val parentRecordId: Long,//		Идентификатор родительской записи
    val modPerson: Long,//		Идентификатор менеджера, который провел или изменил платёж
    val currId: Long,//		Идентификатор валюты из справочника валют
    val orderId: Long,//		Список оплачиваемых счетов(id)	2.0.48
    val status: Long,//		Статуса: 0 — проведен, 1 — проведен при загрузке файла сверки, 2 — аннулирован
    val classId: Long,//		Идентификатор категории платежа
    val amount: Double, //		Величина платежа в валюте договора
    val fromAgrmId: Long,//		Идентификатор договора, с которого был переведен платеж. Если платёж не участвовал в переводе — 0
    val revNo: Long,//		Номер ревизии платежа
    val revisions: Long,//		Количество корректировок платежа. Если платёж только что создан — 1
    val cashCode: Long,//				Название организации	2.0.48
    val className: String,//		Название категории платежа
    val payDate: String,//		Дата и время проведения платежа в формате: ГГГГ-ММ-ДД ЧЧ:ММ:СС
    val localDate: String,//		Дата и время проведения платежа по времени сервера, на котором установлен биллинг
    val cancelDate: String,//		Дата аннулирования платежа. Если платёж не аннулирован — пусто
    val periodDate: String,//		Бухгалтерский период, к которому относится платёж
    val receipt: String,//		Номер платёжного документа (не путать с «платёжным поручением»)
    val comment: String,//		Комментарий
    val uuid: String,//		Идентификатор платежа для интеграции с внешними системами
    val fromAgrmNumber: String,//		Номер договора, с которого был переведен платёж. Если платёж не участвовал в переводе — пусто
    val paymentOrderNumber: String,//		Номер платёжного поручения (не путать с «платёжным документом»)
)