package com.impuls.billing.side_billing.model.out_dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClientAccount(
    @JsonProperty("billDeliveryName")
    val billDeliveryName: String,
    @JsonProperty("account")
    val account: Account,
    @JsonProperty("agreements")
    val agreements: List<Agreement>
)
