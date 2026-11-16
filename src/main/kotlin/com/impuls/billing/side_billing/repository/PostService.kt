package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.model.in_dto.SharedPostResponse
import com.impuls.billing.side_billing.soap.LanBillingClient
import com.impuls.billing.side_billing.utils.toSharedPostResponse
import com.impuls.billing.soap.generated.SoapFilter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PostService(
    private val billingClient: LanBillingClient,
    private val tokenService: TokenService,
) {
    suspend fun getClientSharedPosts(
        authHeader: String,
        dateFrom: String?,
    ): List<SharedPostResponse> {
        val sessionId = tokenService.getSession(authHeader)?.sessionId
            ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)

        val flt = SoapFilter().apply {
            if (!dateFrom.isNullOrEmpty()) this.dtfrom = dateFrom
        }
        val posts = billingClient.execute(sessionId) {
            getClientSharedPosts(flt, emptyList())
        }
        return posts.map { it.toSharedPostResponse() }
    }
}