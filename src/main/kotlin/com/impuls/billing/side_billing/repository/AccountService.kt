package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.AccountBillingService
import com.impuls.billing.side_billing.in_controller.AuthBillingService
import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.model.out_dto.dto.ValidationDto
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class AccountService(
    private val authBillingService: AuthBillingService,
    private val tokenService: TokenService,
    private val accountBillingService: AccountBillingService,
) {

    fun getAccountData(authHeader: String): Mono<ClientAccount> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({ authBillingService.getClientAccount(session.sessionId) })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    @Transactional
    fun setClientMobilePhone(
        authHeader: String,
        uid: Long,
        phone: String,
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.setClientMobilePhone(
                    sessionId = it.sessionId,
                    uid = uid,
                    phone = phone,
                )
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ResultSuccessDto(header == 1L)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    @Transactional
    fun setClientEmail(
        authHeader: String,
        uid: Long,
        email: String,
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.setClientEmail(
                    sessionId = it.sessionId,
                    uid = uid,
                    email = email,
                )
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ResultSuccessDto(header == 1L)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun validateClientMobileNumber(
        authHeader: String,
    ): Mono<ValidationDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.validateClientMobileNumber(
                    sessionId = it.sessionId,
                )
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ValidationDto(recordNumber = header)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun validateClientEmail(
        authHeader: String,
    ): Mono<ValidationDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.validateClientEmail(
                    sessionId = it.sessionId,
                )
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ValidationDto(recordNumber = header)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    @Transactional
    fun confirmValidateClientMobileNumber(
        authHeader: String,
        code: String,
        id: Long
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.confirmValidateClientMobileNumber(
                    sessionId = it.sessionId,
                    code = code,
                    id = id
                )
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ResultSuccessDto(header == 1L)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    @Transactional
    fun confirmValidateClientEmail(
        authHeader: String,
        code: String,
        id: Long
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.confirmValidateClientEmail(
                    sessionId = it.sessionId,
                    code = code,
                    id = id
                )
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ResultSuccessDto(header == 1L)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }


    @Transactional
    fun logout(
        authHeader: String,
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            accountBillingService.logout(sessionId = session.sessionId)
                .map { header ->
                    tokenService.deleteSession(session) // Синхронное удаление
                    ResultSuccessDto(header == 1L)
                }
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

}