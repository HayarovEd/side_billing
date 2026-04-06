package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.in_dto.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.side_billing.utils.SoapParser
import com.impuls.billing.side_billing.utils.handler
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class AccountBillingService(
    private val webClient: WebClient,
) {

    fun setClientMobilePhone(
        uid: Long,
        phone: String,
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:setClientInfo>\n" +
                            "         <val>\n" +
                            "            <userid>${uid}</userid>\n" +
                            "            <mobile>${phone}</mobile>\n" +
                            "         </val>\n" +
                            "      </urn:setClientInfo>"
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

    fun setClientEmail(
        uid: Long,
        email: String,
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:setClientInfo>\n" +
                            "         <val>\n" +
                            "            <userid>${uid}</userid>\n" +
                            "            <email>${email}</email>\n" +
                            "         </val>\n" +
                            "      </urn:setClientInfo>"
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

    fun validateClientMobileNumber(
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:validateClientMobileNumber/>\n"
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

    fun validateClientEmail(
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "  <urn:validateClientEmail/>\n"
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

    fun confirmValidateClientMobileNumber(
        id: Long,
        code: String,
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:confirmValidateClientMobileNumber>\n" +
                            "         <id>$id/id>\n" +
                            "         <code>$code</code>\n" +
                            "      </urn:confirmValidateClientMobileNumber>\n"
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

    fun confirmValidateClientEmail(
        id: Long,
        code: String,
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:confirmValidateClientEmail>\n" +
                            "         <id>$id/id>\n" +
                            "         <code>$code</code>\n" +
                            "      </urn:confirmValidateClientEmail>\n"
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

    fun logout(
        sessionId: String
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:Logout/>\n"
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

    fun getClientVgroups(
        sessionId: String,
        agrmid: Long?,
    ): Mono<List<ClientVgroupFullResponse>> {
        val agrmidStr = agrmid?.toString() ?: ""
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:getClientVgroups>\n" +
                            "         <flt>\n" +
                            "          <agrmid>$agrmidStr</agrmid>\n" +
                            "         </flt>\n" +
                            "      </urn:getClientVgroups>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    SoapParser.parseFullSoapClientVgroupFullResponse(body)
                }
            }
        )
    }
}