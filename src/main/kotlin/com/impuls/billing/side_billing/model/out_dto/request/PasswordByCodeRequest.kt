package com.impuls.billing.side_billing.model.out_dto.request

data class PasswordByCodeRequest (
    val code: String,
    val password: String,
    val login: String,
)