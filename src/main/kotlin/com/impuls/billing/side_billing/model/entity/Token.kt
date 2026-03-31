package com.impuls.billing.side_billing.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import java.time.Instant

@Entity
@Table(name = "access_token")
@SQLDelete(sql = "DELETE FROM access_token WHERE expires_at < NOW()")
data class Token (
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: Long = 0L,
    @Column(name = "expires_at")
    val expiresAt: Instant = Instant.now().plusSeconds(2*3600),
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),
    @Column(name = "token")
    val token: String,
    @Column(name = "sessionId")
    val sessionId: String,
)