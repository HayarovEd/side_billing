package com.impuls.billing.side_billing.model.out_dto.request

import jakarta.validation.constraints.Email

data class EmailUpdateRequest(
    val uid: Int,
    @field:Email(message = "Invalid email format.")
    val email: String,
)