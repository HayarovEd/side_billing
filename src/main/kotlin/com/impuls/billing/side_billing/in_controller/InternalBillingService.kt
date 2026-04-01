package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.utils.toClientAccount
import com.impuls.billing.side_billing.utils.toRecoverPassword
import com.impuls.billing.side_billing.model.in_dto.SessionBilling
import com.impuls.billing.side_billing.model.out_dto.dto.RecoverPassword
import com.impuls.billing.side_billing.utils.SoapParser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class InternalBillingService(
    private val webClient: WebClient,
) {

    fun onLogin(
        username: String,
        password: String
    ): Mono<SessionBilling> {
        val soapRequest = buildString {
            append("""<?xml version="1.0" encoding="UTF-8"?>""")
            append(
                """
                <soapenv:Envelope
                    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:lbapi="urn:api3">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <lbapi:ClientLogin>
                            <login>$username</login>
                            <pass>$password</pass>
                        </lbapi:ClientLogin>
                    </soapenv:Body>
                </soapenv:Envelope>
            """.trimIndent()
            )
        }
        return handler(
            soapRequest = soapRequest,
            sessionId = "",
            bodyFunction = { response ->
                val uid = SoapParser.parseLoginResponse(response.body)
                val sessionId = response.headers.getFirst("Set-Cookie")

                if (uid != null && sessionId != null) {
                    SessionBilling(
                        userId = uid,
                        sessionId = sessionId
                    )
                } else {
                    throw BillingException("User not found", HttpStatus.UNAUTHORIZED)
                }
            }
        )
    }

    fun getClientAccount(sessionId: String): Mono<ClientAccount> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:getClientAccount>\n</urn:getClientAccount>\n"
                )
            )
        }
        return handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    SoapParser.parseFullSoapResponse(body).toClientAccount()
                }
            }
        )
    }


    fun recoverPassword(
        transport: Int,
        login: String,
        sync: Boolean,
        simpleRecover: Boolean,
        operId: Int,
    ): Mono<RecoverPassword> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:recoverPassword>\n" +
                            "<transport>$transport</transport>\n" +
                            "<login>$login</login>\n" +
                            "<sync>$sync</sync>\n" +
                            "<simplerecover>$simpleRecover</simplerecover>\n" +
                            "<operid>$operId</operid>\n" +
                            "</urn:recoverPassword>\n"
                )
            )
        }
        return handler(
            soapRequest = soapRequest,
            sessionId = "",
            bodyFunction = { response ->
                response.body?.let { body ->
                    SoapParser.parseToRecoverPassword(body).toRecoverPassword()
                }
            }
        )
    }

    fun updatePasswordByCode(
        login: String,
        password: String,
        acceptCode: String,
    ): Mono<Int> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:updatePasswordByCode>\n" +
                            "<val>\n" +
                            "<login>$login</login>\n" +
                            "<password>$password</password>\n" +
                            "<acceptcode>$acceptCode</acceptcode>\n" +
                            "</val>\n" +
                            "</urn:updatePasswordByCode>\n"
                )
            )
        }
        return handler(
            soapRequest = soapRequest,
            sessionId = "",
            bodyFunction = { response ->
                response.body?.let { body ->
                    SoapParser.parseSetClientInfoResponse(body)
                }
            }
        )
    }

    private fun <T : Any> handler(
        soapRequest: String,
        sessionId: String,
        bodyFunction: (ResponseEntity<String>) -> T?
    ): Mono<T> {
        return webClient
            .post()
            .bodyValue(soapRequest)
            .header("Set-Cookie", sessionId)
            .retrieve()
            .toEntity(String::class.java)
            .mapNotNull { response ->
                println(response)
                bodyFunction(response)
            }
            .doOnError {
                println(it)
            }
            .onErrorResume(WebClientResponseException::class.java) { ex ->
                println(ex)
                val faultString = SoapParser.parseFaultString(ex.responseBodyAsString)
                if (faultString?.contains("Invalid login", ignoreCase = true) == true) {
                    // Ошибка аутентификации → 401
                    Mono.error(BillingException(faultString, HttpStatus.UNAUTHORIZED))
                } else if (!faultString.isNullOrBlank()) {
                    Mono.error(BillingException(faultString, HttpStatus.BAD_REQUEST))
                    //E-mail for user '$1' is not confirmed
                } else {
                    Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Billing service error", ex))
                }
            }
    }

}