package com.impuls.billing.side_billing.model.in_dto.notices

data class NoticeResponse(
    val type: Long,
    val isEmail: Boolean,
    val isSms: Boolean,
    val isMessenger: Boolean,
    val name: String,
    val descr: String,
    val value: String,
)
