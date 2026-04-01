package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.repository.AccountService
import org.springframework.web.bind.annotation.GetMapping
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
}