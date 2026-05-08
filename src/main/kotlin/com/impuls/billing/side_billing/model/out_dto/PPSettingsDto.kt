package com.impuls.billing.side_billing.model.out_dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.PositiveOrZero
import org.jetbrains.annotations.NotNull

data class PPSettingsDto(
    @get:NotNull
    @get:JsonProperty("promise_rent")
    val promiseRent: Boolean,

    @get:NotNull
    @get:PositiveOrZero
    @get:JsonProperty("promise_till")
    val promiseTill: Long,

    @get:NotNull
    @get:PositiveOrZero
    @get:JsonProperty("promise_min")
    val promiseMin: Double,

    @get:NotNull
    @get:PositiveOrZero
    @get:JsonProperty("promise_max")
    val promiseMax: Double,

    @get:NotNull
    @get:PositiveOrZero
    @get:JsonProperty("promise_limit")
    val promiseLimit: Double,

    @get:NotNull
    @get:PositiveOrZero
    @get:JsonProperty("promise_on_days")
    val promiseOnDays: Long,

    @get:NotNull
    @get:PositiveOrZero
    @get:JsonProperty("promise_block_days")
    val promiseBlockDays: Long,

    @get:NotNull
    @get:JsonProperty("promise_available")
    val promiseAvailable: Boolean,

    @get:NotNull
    @get:PositiveOrZero
    @get:JsonProperty("promise_fee")
    val promiseFee: Double
)
