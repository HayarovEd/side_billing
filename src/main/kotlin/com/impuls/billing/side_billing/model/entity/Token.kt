package com.impuls.billing.side_billing.model.entity

data class Token(
    val id: Long = 0L,
    val token: String = "",
    val sessionId: String = ""
)