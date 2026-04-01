package com.impuls.billing.side_billing.model.out_dto.request

import jakarta.validation.constraints.Pattern

data class PhoneUpdateRequest(
    val uid: Int,
    @field:Pattern(
        regexp = "^\\+?[1-9][0-9]{7,14}$",
        message = "Invalid phone number format. Must be 8-15 digits, optional + prefix"
    )
    val phone: String,
)