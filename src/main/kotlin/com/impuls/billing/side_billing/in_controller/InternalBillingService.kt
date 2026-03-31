package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.out_dto.ClientAccount
import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.mapper.toClientAccount
import com.impuls.billing.side_billing.model.in_dto.SessionBilling
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

    private val soapParser = SoapParser()

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
                val uid = parseLoginResponse(response.body)
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
                setBaseAppend(
                    "<urn:getClientAccount>\n</urn:getClientAccount>\n"
                )
            )
        }
        return webClient
            .post()
            .bodyValue(soapRequest)
            .header("Set-Cookie", sessionId)
            .retrieve()
            .toEntity(String::class.java)
            .onErrorResume(WebClientResponseException::class.java) { ex ->
                val faultString = parseFaultString(ex.responseBodyAsString)
                if (faultString?.contains("Invalid login", ignoreCase = true) == true) {
                    // Ошибка аутентификации → 401
                    Mono.error(BillingException(faultString, HttpStatus.UNAUTHORIZED))
                } else {
                    Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Billing service error", ex))
                }
            }
            .mapNotNull { response ->
                val result = response.body?.let { body ->
                    soapParser.parseFullSoapResponse(body).toClientAccount()
                }
                result
            }
     /*   val a =  handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    soapParser.parseFullSoapResponse(body).toClientAccount()
                }
            }
        )
        return a*/
    }

    private fun <T : Any> handler(
        soapRequest: String,
        sessionId: String,
        bodyFunction: (ResponseEntity<String>) -> T?): Mono<T> {
        return webClient
            .post()
            .bodyValue(soapRequest)
            .header("Set-Cookie", sessionId)
            .retrieve()
            .toEntity(String::class.java)
            .mapNotNull { response ->
                bodyFunction(response)
            }
            .onErrorResume(WebClientResponseException::class.java) { ex ->
                val faultString = parseFaultString(ex.responseBodyAsString)
                if (faultString?.contains("Invalid login", ignoreCase = true) == true) {
                    // Ошибка аутентификации → 401
                    Mono.error(BillingException(faultString, HttpStatus.UNAUTHORIZED))
                } else {
                    Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Billing service error", ex))
                }
            }
    }


    private fun parseLoginResponse(xml: String?): Long? {
        // Простой парсинг через regex (для продакшена лучше использовать XML парсер)
        val uidRegex = Regex("<uid>(\\d+)</uid>")
        return xml?.let {
            uidRegex.find(it)?.groupValues?.get(1)?.toLongOrNull()
        }
    }

    private fun parseFaultString(xml: String?): String? {
        val regex = Regex("<faultstring>(.*?)</faultstring>", RegexOption.DOT_MATCHES_ALL)
        return xml?.let { regex.find(it)?.groupValues?.get(1) }
    }

    private fun setBaseAppend(innerContent: String): String {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:urn=\"urn:api3\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                innerContent +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>".trimIndent()
    }

}