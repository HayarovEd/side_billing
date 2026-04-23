package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.in_dto.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.side_billing.model.in_dto.notices.AccountNoticeResponse
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
        vgId: Long?,
    ): Mono<List<ClientVgroupFullResponse>> {
        val agrmidStr = agrmid?.toString() ?: ""
        val vgidStr = vgId?.toString() ?: ""
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:getClientVgroups>\n" +
                            "         <flt>\n" +
                            "          <agrmid>$agrmidStr</agrmid>\n" +
                            "          <vgid>$vgidStr</vgid>\n" +
                            "          <nodetails>0</nodetails>\n" +
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

    fun updateClientPassword(
        sessionId: String,
        newPassword: String,
        oldPassword: String,
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:updClientPass>\n" +
                            "<oldpass>$oldPassword</oldpass>\n" +
                            "<newpass>$newPassword</newpass>\n" +
                            "</urn:updClientPass>\n"
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


    fun getAccountNotices(
        sessionId: String,
        uid: Long,
    ): Mono<AccountNoticeResponse> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:getAccountNotices>\n" +
                            "         <flt>\n" +
                            "            <userid>$uid</userid>\n" +
                            "         </flt>\n" +
                            "      </urn:getAccountNotices>"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    SoapParser.parseAccountNotices(body)
                }
            }
        )
    }

    fun setAccountNotices(
        sessionId: String,
        accountNoticeResponse: AccountNoticeResponse
    ): Mono<Long> {
        val noticesXml = accountNoticeResponse.notices.joinToString(separator = "\n") { notice ->
            "            <notices>\n" +
                    "                <type>${notice.type}</type>\n" +
                    "               <isemail>${notice.isEmail.toLong()}</isemail>\n" +
                    "               <issms>${notice.isSms.toLong()}</issms>\n" +
                    "               <ismessenger>${notice.isMessenger.toLong()}</ismessenger>\n" +
                    "               <name>${notice.name}</name>\n" +
                    "               <descr>${notice.descr}</descr>\n" +
                    "               <value>${notice.value}</value>\n" +
                    "            </notices>\n"
        }
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:setAccountNotices>\n" +
                            "         <an>\n" +
                            "            <uid>${accountNoticeResponse.uid}</uid>\n" +
                            noticesXml +
                            "         </an>\n" +
                            "      </urn:setAccountNotices>\n"
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

    private fun Boolean.toLong(): Long = if (this) 1L else 0L

}