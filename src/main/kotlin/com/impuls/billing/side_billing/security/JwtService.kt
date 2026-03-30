package com.impuls.billing.side_billing.security


interface JwtService {
    fun generateAccessToken(userId: String): String
    fun validateAccessToken(token: String): Boolean
    fun getUserIdFromToken(token: String): String
}