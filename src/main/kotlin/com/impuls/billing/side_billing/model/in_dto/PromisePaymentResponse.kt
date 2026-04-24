package com.impuls.billing.side_billing.model.in_dto

data class PromisePaymentResponse(
    val recordId: Long,//		Идентификатор записи в БД
    val agrmId: Long,//		Идентификатор договора
    val payId: Long,//		Идентификатор платежа из таблицы payments при погашении обещанного платежа. Принимает значение "-1", если платеж просрочен и предоставленный кредит аннулирован
    val currId: Long,//		Идентификатор валюты, которая используется на договоре
    val debt: Double,//		Непогашенная часть обещанного платежа
    val amount: Double,//		Величина обещанного платежа в валюте договора
    val promiseDate: String,//		Дата подключения обещанного платежа
    val promiseTill: String,//		Дата окончания действия обещанного платежа
    val symbol: String,//		Сокращенное обозначение валюты
    val username: String,//		Имя пользователя
    val agrmNum: String,//		Номер договора, на который подключен обещанный платеж
    val status: Int,//		Статус обещанного платежа
    val modPerson: Long,//		Идентификатор менеджера, совершившего платёж. Принимает значение "-1", если платёж совершил пользователь
    val cancelPerson: Long,//		Идентификатор менеджера, отменившего платёж
    val modFio: String,//		ФИО менеджера или пользователя, совершившего платёж
    val cancelFio: String,//		ФИО менеджера, отменившего платёж
    val fee: Double,//		Плата за взятие обещанного платежа
)
