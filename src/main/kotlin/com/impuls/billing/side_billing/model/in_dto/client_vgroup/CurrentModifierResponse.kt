package com.impuls.billing.side_billing.model.in_dto.client_vgroup

data class CurrentModifierResponse (
    val type: String, //Тип модификатора
    val value: Double, //Коэффициент скидки
)