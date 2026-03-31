package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.model.in_dto.SessionBilling
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.springframework.http.HttpStatus
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

        return webClient
            .post()
            .bodyValue(soapRequest)
            .retrieve()
            .toEntity(String::class.java)
            .mapNotNull { response -> // mapNotNull игнорирует null значения
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
            .onErrorResume(WebClientResponseException::class.java) { ex ->
                // Пытаемся извлечь faultstring из тела ошибки (которое при 500)
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

    fun parseSetClientInfoResponse(xmlString: String): Int {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").first()
        return retElement?.text()?.toIntOrNull()?:-1
    }
}