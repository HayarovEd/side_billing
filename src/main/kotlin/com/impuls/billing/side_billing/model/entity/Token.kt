package com.impuls.billing.side_billing.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import java.time.Instant

@Table(name = "access_token")
@SQLDelete(sql = "DELETE FROM refresh_tokens WHERE expires_at < NOW()")
data class Token (
    @Column(name = "id")
    val id: Long,
    @Column(name = "expires_at")
    val expiresAt: Instant,
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,
    @Column(name = "token")
    val token: String,
    @Column(name = "sessionId")
    val sessionId: String,
)