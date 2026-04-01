package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.AuthBillingService
import com.impuls.billing.side_billing.model.entity.Token
import com.impuls.billing.side_billing.model.out_dto.dto.RecoverPassword
import com.impuls.billing.side_billing.model.out_dto.dto.ResultDto
import com.impuls.billing.side_billing.model.out_dto.dto.TokenDto
import com.impuls.billing.side_billing.security.JwtService
import com.impuls.billing.side_billing.service.AuthRepository
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val jwtService: JwtService,
    private val authBillingService: AuthBillingService,
    private val authRepository: AuthRepository,
) {

    fun onLogin(
        username: String,
        password: String
    ): Mono<TokenDto> {
        val result = authBillingService.onLogin(
            username = username,
            password = password
        )
        return result.map { header ->
            when (header) {
                null -> throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                else -> {
                    val generatedToken = jwtService.generateAccessToken(header.userId.toString())
                    val token = authRepository.save(
                        Token(
                            token = generatedToken,
                            sessionId = header.sessionId
                        )
                    )
                    TokenDto(
                        token = token.token,
                        id = token.id
                    )
                }
            }
        }
    }

    fun recoverPassword(
        transport: Int,
        login: String,
        sync: Boolean,
        simpleRecover: Boolean,
        operId: Int,
    ): Mono<RecoverPassword> {
        return monoMapper({
            authBillingService.recoverPassword(
                transport = transport,
                login = login,
                sync = sync,
                simpleRecover = simpleRecover,
                operId = operId,
            )
        })
    }

    fun updatePasswordByCode(
        login: String,
        password: String,
        acceptCode: String,
    ): Mono<ResultDto> {
        return monoMapper({
            val result = authBillingService.updatePasswordByCode(
                login = login,
                password = password,
                acceptCode = acceptCode
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
    }
}