package com.impuls.billing.side_billing.model.out_dto.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class RecoverPasswordRequest(
    val transport: Int,
    val login: String,
    val sync: Boolean? = false,
    val simpleRecover: Boolean? = false,
    val operId: Int? = 0,
) {
    fun getSync(): Boolean = sync ?: false
    fun getSimpleRecover(): Boolean = simpleRecover ?: false
    fun getOperId(): Int = operId ?: 0
}