package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.model.out_dto.dto.ResultDto
import com.impuls.billing.side_billing.model.out_dto.request.EmailUpdateRequest
import com.impuls.billing.side_billing.model.out_dto.request.PhoneUpdateRequest
import com.impuls.billing.side_billing.repository.AccountService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/account")
class OutAccountController(
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
    ): Mono<ResultDto> {
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
    ): Mono<ResultDto> {
        return accountService
            .setClientEmail(
                authHeader = authHeader,
                uid = body.uid,
                email = body.email,
            )

    }

}