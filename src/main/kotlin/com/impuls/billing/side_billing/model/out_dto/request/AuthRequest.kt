package com.impuls.billing.side_billing.model.out_dto.request

import com.fasterxml.jackson.annotation.JsonAlias

data class AuthRequest(
    @JsonAlias("login", "username")
    val name: String,
    val password: String
)