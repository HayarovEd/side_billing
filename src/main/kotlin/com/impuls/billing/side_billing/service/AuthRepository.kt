package com.impuls.billing.side_billing.service

import com.impuls.billing.side_billing.model.entity.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository:  JpaRepository<Token, Long> {
    fun findByToken(token: String): Token?
}