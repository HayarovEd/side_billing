package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ClientPayment
import com.impuls.billing.side_billing.model.out_dto.dto.PromisePaymentDto
import com.impuls.billing.side_billing.utils.SoapParser
import com.impuls.billing.side_billing.utils.handler
import com.impuls.billing.side_billing.utils.toClientPayments
import com.impuls.billing.side_billing.utils.toPromisePaymentResponse
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



    fun getClientPromisePayments(
        sessionId: String
    ): Mono<List<PromisePaymentDto>> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:getClientPromisePayments>\n" +
                            "      </urn:getClientPromisePayments>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseToPromisePayment(body).map {
                        it.toPromisePaymentResponse()
                    }
                }
            }
        )
    }

    fun getClientPayments(
        sessionId: String,
        agrmNum: String, //agrmnum
        dateTo: String, //dtto
        dateFrom: String, //dtfrom
    ): Mono<List<ClientPayment>> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:getClientPayments>\n" +
                            "<flt>\n"+
                            "<agrmnum>$agrmNum</agrmnum>\n" +
                            "<dtto>$dateTo</dtto>\n" +
                            "<dtfrom>$dateFrom</dtfrom>\n" +
                            "</flt>\n"+
                    "</urn:getClientPayments>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseFullSoapPaymentsResponse(body).toClientPayments()
                }
            }
        )
    }

    fun getClientOrders(
        sessionId: String,
        payed: Long, //payed
        dateTo: String, //dtto
        dateFrom: String, //dtfrom
    ): Mono<List<OrderResponse>> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:getClientOrders>\n" +
                            "         <flt>\n" +
                            "                <dtfrom>$dateFrom</dtfrom>\n" +
                            "                <dtto>$dateTo</dtto>\n" +
                            "                 <payed>$payed</payed>\n" +
                            "         </flt>\n" +
                            "      </urn:getClientOrders>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseOrderResponse(body)
                }
            }
        )
    }
}