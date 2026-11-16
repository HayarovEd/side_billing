package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.out_dto.dto.RecoverPassword
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.model.out_dto.dto.TokenDto
import com.impuls.billing.side_billing.model.out_dto.dto.ValidateToken
import com.impuls.billing.side_billing.model.out_dto.request.AuthRequest
import com.impuls.billing.side_billing.model.out_dto.request.PasswordByCodeRequest
import com.impuls.billing.side_billing.model.out_dto.request.RecoverPasswordRequest
import com.impuls.billing.side_billing.repository.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    suspend fun login(
        @RequestBody body: AuthRequest
    ): TokenDto {
        return authService.onLogin(
            username = body.name,
            password = body.password
        )
    }

    @PostMapping("/recoverPassword")
    suspend fun recoverPassword(
        @RequestBody body: RecoverPasswordRequest
    ): RecoverPassword {
        return authService.recoverPassword(
            transport = body.transport,
            login = body.login,
            sync = body.getSync(),
            simpleRecover = body.getSimpleRecover(),
            operId = body.getOperId(),
        )
    }

    @PostMapping("/updatePasswordByCode")
    suspend fun updatePasswordByCode(
        @RequestBody body: PasswordByCodeRequest
    ): ResultSuccessDto {
        return authService.updatePasswordByCode(
            login = body.login,
            password = body.password,
            acceptCode = body.code
        )
    }

    @GetMapping("/is_valid")
    fun validateToken(
        @RequestParam(name = "token", required = true) token: String,
    ): ValidateToken {
        return authService.checkEnableToken(
            token = token,
        )
    }
}