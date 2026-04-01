package com.impuls.billing.side_billing.out_controller

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.impuls.billing.side_billing.model.out_dto.RecoverPassword
import com.impuls.billing.side_billing.model.out_dto.TokenDto
import com.impuls.billing.side_billing.repository.AuthService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class OutAuthController(
    private val authService: AuthService,
) {
    data class AuthRequest(
        val name: String,
        val password: String
    )


    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class RecoverPasswordRequest(
        val transport: Int,
        val login: String,
        val sync: Boolean? = false,
        val simpleRecover: Boolean? = false,
        val operId: Int? = 0,
    ) {
        fun getSync(): Boolean = sync ?: false
        fun getSimpleRecover(): Boolean = simpleRecover ?: false
        fun getOperId(): Int = operId ?: 0
    }

    /* @PostMapping("/register")
     fun register(
         @Valid @RequestBody body: AuthRequest
     ) {
         authService.register(body.email, body.password)
     }*/

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
}