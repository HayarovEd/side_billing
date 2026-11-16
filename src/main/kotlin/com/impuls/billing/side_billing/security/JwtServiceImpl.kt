package com.impuls.billing.side_billing.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Base64
import java.util.Date

@Service
class JwtServiceImpl(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration:7200000}") private val jwtExpirationMs: Long
): JwtService {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

    private fun generateToken(
        userId: String,
        sessionId: String,
        type: String,
        expiry: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("sid", sessionId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    override fun generateAccessToken(userId: String, sessionId: String): String {
        return generateToken(userId, sessionId, "access", jwtExpirationMs)
    }

    override fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "access"
    }

    override fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid token."
        )
        return claims.subject
    }

    override fun getSessionIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid token."
        )
        return claims["sid"] as? String ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid token session."
        )
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) {
            token.removePrefix("Bearer ").trim()
        } else token.trim()
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }
}