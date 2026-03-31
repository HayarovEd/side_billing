package com.impuls.billing.side_billing.out_controller

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



    /* @PostMapping("/refresh")
     fun refresh(
         @RequestBody body: RefreshRequest
     ): AuthService.TokenPair {
         return authService.refresh(body.refreshToken)
     }*/
}