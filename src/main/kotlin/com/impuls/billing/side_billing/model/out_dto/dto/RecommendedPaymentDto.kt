package com.impuls.billing.side_billing.model.out_dto.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.annotations.NotNull

data class RecommendedPaymentDto(
    @get:NotNull
    @get:JsonProperty("recommended_payment")
    val amount: Double,
)
