package com.impuls.billing.side_billing.model.out_dto.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class ValidateToken(
    @JsonProperty("is_valid")
    val isValid: Boolean
) {
    @get:JsonIgnore  // игнорируем геттер при сериализации
    val valid get() = isValid
}