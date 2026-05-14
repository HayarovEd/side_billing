package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.PostBillingService
import com.impuls.billing.side_billing.model.in_dto.SharedPostResponse
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PostService(
    private val postBillingService: PostBillingService,
    private val tokenService: TokenService,
) {
    fun getClientSharedPosts(
        authHeader: String,
        dateFrom: String?,
    ): Mono<List<SharedPostResponse>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                postBillingService.getClientSharedPosts(
                    sessionId = it.sessionId,
                    dateFrom = dateFrom,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }
}