package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.service.AuthRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TokenCleanupService(
    private val authRepository: AuthRepository
) {

    @Scheduled(cron = "0 0 */1 * * *") // Каждый час
    @Transactional
    fun cleanExpiredTokens() {
        val deletedCount = authRepository.deleteExpiredTokens()

        println("Удалено $deletedCount просроченных токенов")
    }
}