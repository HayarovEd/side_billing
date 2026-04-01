package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.AccountBillingService
import com.impuls.billing.side_billing.in_controller.AuthBillingService
import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.model.out_dto.dto.ResultDto
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
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

    fun setClientMobilePhone(
        authHeader: String,
        uid: Int,
        phone: String,
    ): Mono<ResultDto> {
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
                            ResultDto(header == 1)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun setClientEmail(
        authHeader: String,
        uid: Int,
        email: String,
    ): Mono<ResultDto> {
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
                            ResultDto(header == 1)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun validateClientMobileNumber(
        authHeader: String,
    ): Mono<ResultDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.validateClientMobileNumber(
                    sessionId = it.sessionId,)
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ResultDto(header == 1)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun validateClientEmail(
        authHeader: String,
    ): Mono<ResultDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = accountBillingService.validateClientEmail(
                    sessionId = it.sessionId,)
                result.map { header ->
                    when (header) {
                        null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                        else -> {
                            ResultDto(header == 1)
                        }
                    }
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

}