package com.impuls.billing.side_billing.model.in_dto

data class PPSettingsResponse (
    val promiseRent: Long, //Флаг «Максимальная сумма — не более абонентской платы»
    val promiseTill: Long, //Срок погашения платежа с момента подключения
    val promiseMin: Double, //Минимальная сумма
    val promiseMax: Double, //Максимальная сумма
    val promiseLimit: Double, //Количество обещанных платежей, которые могут действовать одновременно
    val promiseOnDays: Long, //Минимальное количество дней с момента заключения договора для возможности использования обещанных платежей
    val promiseBlockDays: Long, //Количество дней, в которые абонент не сможет взять обещанный платёж после несвоевременной оплаты
    val promiseAvailable: Long, //Флаг «Обещанный платеж доступен»
    val promiseFee: Double, //Плата за взятие обещанного платежа
)