package com.impuls.billing.side_billing.model.out_dto.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Account (
    @JsonProperty("uid")
    val uid: Int,

    @JsonProperty("doctype")
    val doctype: Int,

    @JsonProperty("billdelivery")
    val billDelivery: Int,

    @JsonProperty("category")
    val category: Int,

    @JsonProperty("type")
    val type: Int,

    @JsonProperty("oksm")
    val oksm: Int,

    @JsonProperty("wrongactive")
    val wrongActive: Int,

    @JsonProperty("archive")
    val archive: Int,

    @JsonProperty("ownership")
    val ownerShip: Int,

    @JsonProperty("mobileisconfirmed")
    val mobileIsConfirmed: Boolean,

    @JsonProperty("emailisconfirmed")
    val emailIsConfirmed: Boolean,

    @JsonProperty("offerisaccepted")
    val offerIsAccepted: Boolean,

    @JsonProperty("soleproprietor")
    val soleProprietor: Boolean,

    @JsonProperty("login")
    val login: String,

    @JsonProperty("pass")
    val password: String,

    @JsonProperty("descr")
    val description: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("phone")
    val phone: String,

    @JsonProperty("fax")
    val fax: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("mobile")
    val mobile: String,

    @JsonProperty("bankname")
    val bankName: String,

    @JsonProperty("branchbankname")
    val branchBankName: String,

    @JsonProperty("treasuryname")
    val treasuryName: String,

    @JsonProperty("treasuryaccount")
    val treasuryAccount: String,

    @JsonProperty("bik")
    val bik: String,

    @JsonProperty("settl")
    val setti: String,

    @JsonProperty("corr")
    val corr: String,

    @JsonProperty("kpp")
    val kpp: String,

    @JsonProperty("inn")
    val inn: String,

    @JsonProperty("ogrn")
    val ogrn: String,

    @JsonProperty("okpo")
    val okpo: String,

    @JsonProperty("okved")
    val okved: String,

    @JsonProperty("abonentname")
    val abonentName: String,

    @JsonProperty("abonentsurname")
    val abonentSurname: String,

    @JsonProperty("abonentpatronymic")
    val abonentPatronymic: String
    )