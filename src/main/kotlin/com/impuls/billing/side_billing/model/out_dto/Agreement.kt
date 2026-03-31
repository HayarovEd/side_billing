package com.impuls.billing.side_billing.model.out_dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Agreement(
    @JsonProperty("agrmid")
    val agrmId: Int,

    @JsonProperty("uid")
    val uid: Int,

    @JsonProperty("operid")
    val operId: Int,

    @JsonProperty("curid")
    val curId: Int,

    @JsonProperty("bnotify")
    val bNotify: Int,

    @JsonProperty("vgroups")
    val vGroups: Int,

    @JsonProperty("penaltymethod")
    val penaltyMethod: Int,

    @JsonProperty("monthblockday")
    val monthBlockDay: Int,

    @JsonProperty("balance")
    val balance: Double,

    @JsonProperty("credit")
    val credit: Double,

    @JsonProperty("code")
    val code: String,

    @JsonProperty("promisecredit")
    val promiseCredit: Double,

    @JsonProperty("installments")
    val installments: Double,

    @JsonProperty("balancestatus")
    val balanceStatus: Int,

    @JsonProperty("friendagrmid")
    val friendAgrmId: Int,

    @JsonProperty("paymentmethod")
    val paymentMethod: Int,

    @JsonProperty("blockdays")
    val blockDays: Int,

    @JsonProperty("blockmonths")
    val blockMonths: Int,

    @JsonProperty("orderpayday")
    val orderPayDay: Int,

    @JsonProperty("blockorders")
    val blockOrders: Int,

    @JsonProperty("blockamount")
    val blockAmount: Double,

    @JsonProperty("ownerid")
    val ownerId: Int,

    @JsonProperty("isdefault")
    val isDefault: Int,

    @JsonProperty("number")
    val number: String,

    @JsonProperty("date")
    val date: String,

    @JsonProperty("closedon")
    val closeDon: String? = null,

    @JsonProperty("bcheck")
    val bCheck: String? = null,

    @JsonProperty("symbol")
    val symbol: String,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("initialbalance")
    val initialBalance: Double
)