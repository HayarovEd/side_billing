package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.client_vgroup.ClientVgroupFullResponse
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
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping("/")
    fun loggedUser(@RequestHeader("Authorization") authHeader: String): Mono<ClientAccount> {
        return accountService
            .getAccountData(authHeader)

    }

    @PostMapping("/clientMobilePhone")
    fun setClientMobilePhone(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: PhoneUpdateRequest
    ): Mono<ResultSuccessDto> {
        return accountService
            .setClientMobilePhone(
                authHeader = authHeader,
                uid = body.uid,
                phone = body.phone,
            )
    }

    @PostMapping("/clientEmail")
    fun setClientEmail(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: EmailUpdateRequest
    ): Mono<ResultSuccessDto> {
        return accountService
            .setClientEmail(
                authHeader = authHeader,
                uid = body.uid,
                email = body.email,
            )
    }

    @PostMapping("/validationClientMobileNumber")
    fun validateClientMobileNumber(
        @RequestHeader("Authorization") authHeader: String,
    ): Mono<ValidationDto> {
        return accountService
            .validateClientMobileNumber(
                authHeader = authHeader,
            )
    }

    @PostMapping("/validationClientEmail")
    fun validateClientEmail(
        @RequestHeader("Authorization") authHeader: String,
    ): Mono<ValidationDto> {
        return accountService
            .validateClientEmail(
                authHeader = authHeader,
            )
    }

    @PostMapping("/confirmationClientMobileNumber")
    fun confirmValidateClientMobileNumber(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: ValidationRequest
    ): Mono<ResultSuccessDto> {
        return accountService
            .confirmValidateClientMobileNumber(
                authHeader = authHeader,
                code = body.code,
                id = body.id,
            )
    }

    @PostMapping("/confirmationClientEmail")
    fun confirmValidateClientEmail(
        @RequestHeader("Authorization") authHeader: String,
        @Valid @RequestBody body: ValidationRequest
    ): Mono<ResultSuccessDto> {
        return accountService
            .confirmValidateClientEmail(
                authHeader = authHeader,
                code = body.code,
                id = body.id,
            )
    }

    @DeleteMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") authHeader: String,
    ): Mono<ResultSuccessDto> {
        return accountService
            .logout(
                authHeader = authHeader
            )
    }

    @GetMapping("/vgroup")
    fun getClientVgroups(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "agrmid", required = false) agrmNum: Long?,
        @RequestParam(name = "vgid", required = false) vgId: Long?,
    ): Mono<List<ClientVgroupFullResponse>> {
        return accountService.getClientVgroups(
            authHeader = authHeader,
            agrmid = agrmNum,
            vgId = vgId
        )
    }

    @PostMapping("/password")
    fun updateClientPassword(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: UpdatePasswordRequest
    ): Mono<ResultSuccessDto> {
        return accountService.updateClientPassword(
            authHeader = authHeader,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword,
        )
    }

}