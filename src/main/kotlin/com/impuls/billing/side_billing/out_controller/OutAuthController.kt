package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.out_dto.dto.RecoverPassword
import com.impuls.billing.side_billing.model.out_dto.dto.TokenDto
import com.impuls.billing.side_billing.model.out_dto.request.AuthRequest
import com.impuls.billing.side_billing.model.out_dto.request.PasswordByCodeRequest
import com.impuls.billing.side_billing.model.out_dto.request.RecoverPasswordRequest
import com.impuls.billing.side_billing.repository.AuthService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class OutAuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(
        @RequestBody body: AuthRequest
    ): Mono<TokenDto> {
        return authService.onLogin(
            username = body.name,
            password = body.password
        )
    }


    @PostMapping("/recoverPassword")
    fun refresh(
        @RequestBody body: RecoverPasswordRequest
    ): Mono<RecoverPassword> {
        return authService.recoverPassword(
            transport = body.transport,
            login = body.login,
            sync = body.getSync(),
            simpleRecover = body.getSimpleRecover(),
            operId = body.getOperId(),
        )
    }

    @PostMapping("/updatePasswordByCode")
    fun updatePasswordByCode(
        @RequestBody body: PasswordByCodeRequest
    ): Mono<Boolean> {
        return authService.updatePasswordByCode(
            login = body.login,
            password = body.password,
            acceptCode = body.code
        )
    }
}