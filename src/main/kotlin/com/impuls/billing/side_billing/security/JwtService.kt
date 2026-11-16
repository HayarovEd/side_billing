package com.impuls.billing.side_billing.security

interface JwtService {
    fun generateAccessToken(userId: String, sessionId: String): String
    fun validateAccessToken(token: String): Boolean
    fun getUserIdFromToken(token: String): String
    fun getSessionIdFromToken(token: String): String
}