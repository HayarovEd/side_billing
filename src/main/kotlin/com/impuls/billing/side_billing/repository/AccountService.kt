package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.model.in_dto.ClientInfoResponse
import com.impuls.billing.side_billing.model.in_dto.PasswordResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.side_billing.model.in_dto.notices.AccountNoticeResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.model.out_dto.dto.ValidationDto
import com.impuls.billing.side_billing.soap.LanBillingClient
import com.impuls.billing.side_billing.utils.*
import com.impuls.billing.soap.generated.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val tokenService: TokenService,
    private val billingClient: LanBillingClient,
) {

    private fun getSessionOrThrow(authHeader: String): String {
        return tokenService.getSession(authHeader)?.sessionId
            ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    suspend fun getAccountData(authHeader: String): ClientAccount {
        val sessionId = getSessionOrThrow(authHeader)
        val accounts = billingClient.execute(sessionId) {
            getClientAccount(SoapGetAccountFilter())
        }
        val firstAccount = accounts.firstOrNull()
            ?: throw BillingException("Account not found", HttpStatus.NOT_FOUND)
        return firstAccount.toClientAccount()
    }

    suspend fun setClientMobilePhone(
        authHeader: String,
        uid: Long,
        phone: String,
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val info = SoapClientInfo().apply {
            this.userid = uid
            this.mobile = phone
        }
        val res = billingClient.execute(sessionId) {
            setClientInfo(info)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun setClientEmail(
        authHeader: String,
        uid: Long,
        email: String,
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val info = SoapClientInfo().apply {
            this.userid = uid
            this.email = email
        }
        val res = billingClient.execute(sessionId) {
            setClientInfo(info)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun validateClientMobileNumber(authHeader: String): ValidationDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            validateClientMobileNumber()
        }
        return ValidationDto(recordNumber = res)
    }

    suspend fun validateClientEmail(authHeader: String): ValidationDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            validateClientEmail()
        }
        return ValidationDto(recordNumber = res)
    }

    suspend fun confirmValidateClientMobileNumber(
        authHeader: String,
        code: String,
        id: Long
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            confirmValidateClientMobileNumber(id, code)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun confirmValidateClientEmail(
        authHeader: String,
        code: String,
        id: Long
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            confirmValidateClientEmail(id, code)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun logout(authHeader: String): ResultSuccessDto {
        val session = tokenService.getSession(authHeader)
        return if (session != null) {
            try {
                val res = billingClient.execute(session.sessionId) { logout() }
                tokenService.deleteSession(session)
                ResultSuccessDto(res == 1L)
            } catch (e: Exception) {
                ResultSuccessDto(true)
            }
        } else {
            ResultSuccessDto(true)
        }
    }

    suspend fun getClientVgroups(
        authHeader: String,
        agrmid: Long?,
        vgId: Long?,
    ): List<ClientVgroupFullResponse> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            if (agrmid != null) this.agrmid = agrmid
            if (vgId != null) this.vgid = vgId
        }
        val vgroups = billingClient.execute(sessionId) {
            getClientVgroups(flt)
        }
        return vgroups.map { it.toClientVgroupFull() }
    }

    suspend fun getClientVgroupPassword(
        authHeader: String,
        vgId: Long,
    ): PasswordResponse {
        val sessionId = getSessionOrThrow(authHeader)
        val pass = billingClient.execute(sessionId) {
            getClientVgroupPassword(vgId)
        }
        return PasswordResponse(password = pass ?: "")
    }

    suspend fun updateClientPassword(
        authHeader: String,
        oldPassword: String,
        newPassword: String,
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            updClientPass(null, oldPassword, newPassword)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun getClientInfo(authHeader: String): ClientInfoResponse {
        val sessionId = getSessionOrThrow(authHeader)
        val infoList = billingClient.execute(sessionId) {
            getClientInfo(SoapFilter())
        }
        val info = infoList.firstOrNull()
            ?: throw BillingException("Client info not found", HttpStatus.NOT_FOUND)
        return info.toClientInfoResponse()
    }

    suspend fun getAccountNotices(
        authHeader: String,
        uid: Long,
    ): AccountNoticeResponse {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply { this.userid = uid }
        val notices = billingClient.execute(sessionId) {
            getAccountNotices(flt)
        }
        val first = notices.firstOrNull()
        return first?.toAccountNoticeResponse() ?: AccountNoticeResponse(uid = uid, notices = emptyList())
    }

    suspend fun setAccountNotices(
        authHeader: String,
        accountNoticeResponse: AccountNoticeResponse,
    ): Long {
        val sessionId = getSessionOrThrow(authHeader)
        val an = SoapAccountNotices().apply {
            this.uid = accountNoticeResponse.uid
            this.notices.addAll(accountNoticeResponse.notices.map {
                SoapAccountNotice().apply {
                    this.type = it.type
                    this.isemail = if (it.isEmail) 1L else 0L
                    this.issms = if (it.isSms) 1L else 0L
                    this.ismessenger = if (it.isMessenger) 1L else 0L
                    this.name = it.name
                    this.descr = it.descr
                    this.value = it.value
                }
            })
        }
        return billingClient.execute(sessionId) {
            setAccountNotices(an)
        }
    }
}