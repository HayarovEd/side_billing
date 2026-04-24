package com.impuls.billing.side_billing.model.out_dto.request

import jakarta.validation.constraints.Pattern

data class ValidationRequest(
    @field:Pattern(
        regexp = "^\\d+$",
        message = "Field must contain only digits"
    )
    val code: String,
    val id: Long
)