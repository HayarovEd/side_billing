package com.impuls.billing.side_billing.model.out_dto.request

data class UpdatePasswordRequest(
    val newPassword: String,
    val oldPassword: String,
)
