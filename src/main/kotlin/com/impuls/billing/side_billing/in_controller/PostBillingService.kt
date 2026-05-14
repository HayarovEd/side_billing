package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.in_dto.SharedPostResponse
import com.impuls.billing.side_billing.utils.SoapParser
import com.impuls.billing.side_billing.utils.handler
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class PostBillingService(
    private val webClient: WebClient,
) {
    fun getClientSharedPosts(
        sessionId: String,
        dateFrom: String?,
    ): Mono<List<SharedPostResponse>> {
        val dateFromString = dateFrom ?: ""
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:getClientSharedPosts>\n" +
                            "         <flt>\n" +
                            "         <dtfrom>$dateFromString</dtfrom>\n" +
                            "         </flt>\n" +
                            "      </urn:getClientSharedPosts>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    SoapParser.parseSharedPosts(body)
                }
            }
        )
    }
}