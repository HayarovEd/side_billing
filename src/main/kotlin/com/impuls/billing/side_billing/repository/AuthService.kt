package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.model.out_dto.dto.RecoverPassword
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.model.out_dto.dto.TokenDto
import com.impuls.billing.side_billing.model.out_dto.dto.ValidateToken
import com.impuls.billing.side_billing.security.JwtService
import com.impuls.billing.side_billing.soap.LanBillingClient
import com.impuls.billing.soap.generated.SoapUpdatePasswordByCode
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val billingClient: LanBillingClient,
) {

    suspend fun onLogin(
        username: String,
        password: String
    ): TokenDto {
        val (uid, sessionId) = billingClient.login(username, password)
        val token = jwtService.generateAccessToken(uid.toString(), sessionId)
        return TokenDto(
            token = token,
            id = uid
        )
    }

    suspend fun recoverPassword(
        transport: Int,
        login: String,
        sync: Boolean,
        simpleRecover: Boolean,
        operId: Int,
    ): RecoverPassword {
        val list = billingClient.execute {
            recoverPassword(transport, login, sync, simpleRecover, operId)
        }
        val res = list?.firstOrNull()
        return RecoverPassword(
            recordId = res?.recordid?.toInt() ?: 0,
            destination = res?.destination ?: ""
        )
    }

    suspend fun updatePasswordByCode(
        login: String,
        password: String,
        acceptCode: String,
    ): ResultSuccessDto {
        val res = billingClient.execute {
            val req = SoapUpdatePasswordByCode().apply {
                this.login = login
                this.password = password
                this.acceptcode = acceptCode
            }
            updatePasswordByCode(req)
        }
        return ResultSuccessDto(!res.isNullOrBlank())
    }

    fun checkEnableToken(token: String): ValidateToken {
        return ValidateToken(jwtService.validateAccessToken(token))
    }
}