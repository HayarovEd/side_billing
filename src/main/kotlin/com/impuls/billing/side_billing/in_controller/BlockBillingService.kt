package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.utils.SoapParser
import com.impuls.billing.side_billing.utils.handler
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class BlockBillingService(
    private val webClient: WebClient,
) {
    fun insertBlkRasp(
        sessionId: String,
        vgId: Long,
        agrmId: Long,
        blkReq: Long,
        changeTime: String,
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "  <urn:insBlkRasp>\n" +
                            "         <val>\n" +
                            "            <vgid>$vgId</vgid>\n" +
                            "            <agrmid>$agrmId</agrmid>\n" +
                            "            <changetime>$changeTime</changetime>\n" +
                            "            <blkreq>$blkReq</blkreq>\n" +
                            "         </val>\n" +
                            "      </urn:insBlkRasp>\n"
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