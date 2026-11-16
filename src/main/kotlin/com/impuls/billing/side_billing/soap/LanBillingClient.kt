package com.impuls.billing.side_billing.soap

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.soap.generated.Api3PortType
import com.impuls.billing.soap.generated.SoapClientLogin
import jakarta.xml.ws.BindingProvider
import jakarta.xml.ws.handler.MessageContext
import jakarta.xml.ws.soap.SOAPFaultException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class LanBillingClient(
    private val port: Api3PortType,
) {
    private val log = LoggerFactory.getLogger(LanBillingClient::class.java)

    suspend fun <T> execute(sessionId: String? = null, block: Api3PortType.() -> T): T = withContext(Dispatchers.IO) {
        val bp = port as BindingProvider
        bp.requestContext["thread.local.request.context"] = "true"
        if (!sessionId.isNullOrBlank()) {
            bp.requestContext[MessageContext.HTTP_REQUEST_HEADERS] = mapOf(
                "Cookie" to listOf(sessionId),
                "Set-Cookie" to listOf(sessionId)
            )
        } else {
            bp.requestContext.remove(MessageContext.HTTP_REQUEST_HEADERS)
        }

        try {
            port.block()
        } catch (e: SOAPFaultException) {
            val faultString = e.fault?.faultString ?: e.message ?: "SOAP Error"
            log.error("SOAP fault: {}", faultString)
            if (faultString.contains("Invalid login", ignoreCase = true) ||
                faultString.contains("Session", ignoreCase = true) ||
                faultString.contains("User not found", ignoreCase = true)
            ) {
                throw BillingException(faultString, HttpStatus.UNAUTHORIZED)
            }
            throw BillingException(faultString, HttpStatus.BAD_REQUEST)
        } catch (e: BillingException) {
            throw e
        } catch (e: Exception) {
            log.error("Billing communication error", e)
            val msg = e.message ?: "Billing communication error"
            val status = if (msg.contains("timed out", ignoreCase = true) || msg.contains("Timeout", ignoreCase = true)) {
                HttpStatus.GATEWAY_TIMEOUT
            } else {
                HttpStatus.BAD_GATEWAY
            }
            throw BillingException(msg, status)
        }
    }

    suspend fun login(username: String, pass: String): Pair<Long, String> = withContext(Dispatchers.IO) {
        val bp = port as BindingProvider
        bp.requestContext["thread.local.request.context"] = "true"
        bp.requestContext.remove(MessageContext.HTTP_REQUEST_HEADERS)

        val result: List<SoapClientLogin>
        try {
            result = port.clientLogin(null, username, pass, null)
        } catch (e: SOAPFaultException) {
            val msg = e.fault?.faultString ?: e.message ?: "Invalid login or password"
            log.warn("SOAP fault during login for user {}: {}", username, msg)
            throw BillingException(msg, HttpStatus.UNAUTHORIZED)
        } catch (e: BillingException) {
            throw e
        } catch (e: Exception) {
            log.error("Error communicating with billing during login for user {}: {}", username, e.message, e)
            val msg = e.message ?: "Billing communication error"
            val status = if (msg.contains("timed out", ignoreCase = true) || msg.contains("Timeout", ignoreCase = true)) {
                HttpStatus.GATEWAY_TIMEOUT
            } else {
                HttpStatus.BAD_GATEWAY
            }
            throw BillingException(msg, status)
        }

        val uid = result.firstOrNull()?.uid
            ?: throw BillingException("User not found", HttpStatus.UNAUTHORIZED)

        val respHeaders = bp.responseContext[MessageContext.HTTP_RESPONSE_HEADERS] as? Map<*, *>
        val rawCookie = respHeaders?.entries
            ?.firstOrNull { it.key.toString().equals("Set-Cookie", ignoreCase = true) }
            ?.value

        val sessionId = when (rawCookie) {
            is List<*> -> rawCookie.firstOrNull()?.toString()
            is String -> rawCookie
            else -> null
        } ?: throw BillingException("Session cookie not returned by billing", HttpStatus.UNAUTHORIZED)

        Pair(uid, sessionId)
    }
}
