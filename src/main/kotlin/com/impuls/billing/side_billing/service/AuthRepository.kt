package com.impuls.billing.side_billing.service

import com.impuls.billing.side_billing.model.entity.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface AuthRepository:  JpaRepository<Token, Long> {
    fun findByToken(token: String): Token?

    @Modifying
    @Query("DELETE FROM Token t WHERE t.expiresAt < CURRENT_TIMESTAMP")
    fun deleteExpiredTokens(): Int

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.id = :id")
    fun deleteByIdNative(@Param("id") id: Long): Int

}