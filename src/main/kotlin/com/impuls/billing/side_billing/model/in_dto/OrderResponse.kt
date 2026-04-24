package com.impuls.billing.side_billing.model.in_dto

data class OrderResponse(
    /**
     * Идентификатор документа
     */
    val orderId: Long,

    /**
     * Идентификатор договора, к которому относится документ
     */
    val agrmId: Long,

    /**
     * Идентификатор оператора
     */
    val operId: Long,

    /**
     * Идентификатор шаблона документа, по которому сгенерирован документ
     */
    val docId: Long,

    /**
     * Номер документа
     */
    val orderNum: String,


    /**
     * Общая сумма документа в валюте договора
     */
    val currSumm: Double,

    /**
     * Сумма налога в валюте договора
     */
    val taxSumm: Double,

    /**
     * Идентификатор пользователя, для которого сгенерирован документ
     */
    val uid: Long,

    /**
     * Как документ должен оплачиваться:
     * * 0 — без оплаты
     * * 1 — постоплата
     * * 2 — предоплата
     * * 3 — предоплата с учётом услуг
     */
    val docPayable: Long,

    /**
     * Флаг «Договор удалён»
     */
    val agrmArchive: Long,

    /**
     * Первый день отчётного месяца
     */
    val period: String,

    /**
     * Дата выставления документа
     */
    val orderDate: String,

    /**
     * Дата генерации документа
     */
    val creationDate: String,

    /**
     * Дата оплаты документа
     */
    val payDate: String,

    /**
     * Имя файла документа
     */
    val filename: String,

    /**
     * Имя шаблона
     */
    val docName: String,

    /**
     * Путь к папке для сохранения сгенерированных по шаблону документов
     */
    val docSavePath: String,

    /**
     * Имя файла шаблона на диске
     */
    val docTemplate: String,

    /**
     * Тип MIME для сгенерированных файлов
     */
    val docuPloadext: String,

    /**
     * Имя оператора, оказавшего услугу
     */
    val operName: String,

    /**
     * Номер договора
     */
    val agrmNum: String,

    /**
     * Имя пользователя, которому сгенерирован документ
     */
    val username: String,

    /**
     * Идентификатор документа, подтверждающего платёж для этого документа
     */
    val payReceipt: String,

    /**
     * Валюта документа
     */
    val currSymbol: String,

    /**
     * Идентификатор метода доставки счёта
     */
    val billDelivery: Long,

    /**
     * Название метода доставки счёта
     */
    val billDeliveryName: String,

    /**
     * Статус выгрузки документа
     */
    val exportState: Short,

    /**
     * Дата выгрузки документа
     */
    val exportDate: String,

    /**
     * Идентификатор организации, которой принадлежит документ
     * Доступно с версии 2.0.48
     */
    val organizationId: Long,

    /**
     * Название организации, к которой относится документ
     */
    val organizationName: String
)
