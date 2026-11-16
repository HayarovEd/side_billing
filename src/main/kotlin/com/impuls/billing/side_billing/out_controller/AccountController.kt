package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.ClientInfoResponse
import com.impuls.billing.side_billing.model.in_dto.PasswordResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.side_billing.model.in_dto.notices.AccountNoticeResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.model.out_dto.dto.ValidationDto
import com.impuls.billing.side_billing.model.out_dto.request.EmailUpdateRequest
import com.impuls.billing.side_billing.model.out_dto.request.PhoneUpdateRequest
import com.impuls.billing.side_billing.model.out_dto.request.UpdatePasswordRequest
import com.impuls.billing.side_billing.model.out_dto.request.ValidationRequest
import com.impuls.billing.side_billing.repository.AccountService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping("/")
    suspend fun loggedUser(@RequestHeader("Authorization") authHeader: String): ClientAccount {
        return accountService.getAccountData(authHeader)
    }

    @PostMapping("/clientMobilePhone")
    suspend fun setClientMobilePhone(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: PhoneUpdateRequest
    ): ResultSuccessDto {
        return accountService.setClientMobilePhone(
            authHeader = authHeader,
            uid = body.uid,
            phone = body.phone,
        )
    }

    @PostMapping("/clientEmail")
    suspend fun setClientEmail(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: EmailUpdateRequest
    ): ResultSuccessDto {
        return accountService.setClientEmail(
            authHeader = authHeader,
            uid = body.uid,
            email = body.email,
        )
    }

    @PostMapping("/validationClientMobileNumber")
    suspend fun validateClientMobileNumber(
        @RequestHeader("Authorization") authHeader: String,
    ): ValidationDto {
        return accountService.validateClientMobileNumber(
            authHeader = authHeader,
        )
    }

    @PostMapping("/validationClientEmail")
    suspend fun validateClientEmail(
        @RequestHeader("Authorization") authHeader: String,
    ): ValidationDto {
        return accountService.validateClientEmail(
            authHeader = authHeader,
        )
    }

    @PostMapping("/confirmationClientMobileNumber")
    suspend fun confirmValidateClientMobileNumber(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: ValidationRequest
    ): ResultSuccessDto {
        return accountService.confirmValidateClientMobileNumber(
            authHeader = authHeader,
            code = body.code,
            id = body.id,
        )
    }

    @PostMapping("/confirmationClientEmail")
    suspend fun confirmValidateClientEmail(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: ValidationRequest
    ): ResultSuccessDto {
        return accountService.confirmValidateClientEmail(
            authHeader = authHeader,
            code = body.code,
            id = body.id,
        )
    }

    @DeleteMapping("/logout")
    suspend fun logout(
        @RequestHeader("Authorization") authHeader: String,
    ): ResultSuccessDto {
        return accountService.logout(
            authHeader = authHeader
        )
    }

    @GetMapping("/vgroup")
    suspend fun getClientVgroups(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrmid", required = false) agrmNum: Long?,
        @RequestParam(name = "vgid", required = false) vgId: Long?,
    ): List<ClientVgroupFullResponse> {
        return accountService.getClientVgroups(
            authHeader = authHeader,
            agrmid = agrmNum,
            vgId = vgId
        )
    }

    @GetMapping("/vgroupPassword")
    suspend fun getClientVgroupPassword(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgid", required = true) vgId: Long,
    ): PasswordResponse {
        return accountService.getClientVgroupPassword(
            authHeader = authHeader,
            vgId = vgId
        )
    }

    @PostMapping("/password")
    suspend fun updateClientPassword(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: UpdatePasswordRequest
    ): ResultSuccessDto {
        return accountService.updateClientPassword(
            authHeader = authHeader,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword,
        )
    }

    @GetMapping("/info")
    suspend fun getClientInfo(
        @RequestHeader("Authorization") authHeader: String
    ): ClientInfoResponse {
        return accountService.getClientInfo(
            authHeader = authHeader,
        )
    }

    @GetMapping("/notices")
    suspend fun getAccountNotices(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "userid", required = true) uid: Long,
    ): AccountNoticeResponse {
        return accountService.getAccountNotices(
            authHeader = authHeader,
            uid = uid
        )
    }

    @PostMapping("/notices")
    suspend fun setAccountNotices(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: AccountNoticeResponse
    ): Long {
        return accountService.setAccountNotices(
            authHeader = authHeader,
            accountNoticeResponse = body
        )
    }
}