package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.utils.SoapParser
import com.impuls.billing.side_billing.utils.handler
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class PaymentBillingService(
    private val webClient: WebClient,
) {
    fun addClientPromisePayment(
        agreementId: Long,
        sum: Double,
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:ClientPromisePayment>\n" +
                            "         <agrm>$agreementId</agrm>\n" +
                            "         <summ>$sum</summ>\n" +
                            "      </urn:ClientPromisePayment>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    SoapParser.parseSetClientInfoResponse(body)
                }
            }
        )
    }
}