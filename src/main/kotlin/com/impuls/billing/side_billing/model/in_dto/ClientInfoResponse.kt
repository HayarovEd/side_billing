package com.impuls.billing.side_billing.model.in_dto

data class ClientInfoResponse(
    val mobileIsConfirmed: Boolean,
    val emailIsConfirmed: Boolean,
    val offerIsAccepted: Boolean,
    val passIsTemporary: Boolean,
    val triggerContactDataReminder: Boolean,
    val email: String,
    val password: String,
    val phone: String,
    val kontPerson: String,
    val mobile: String,
)