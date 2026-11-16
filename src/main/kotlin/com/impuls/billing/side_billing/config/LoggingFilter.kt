package com.impuls.billing.side_billing.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class LoggingFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.debug("=== INCOMING REQUEST: {} {} ===", request.method, request.requestURI)
        try {
            filterChain.doFilter(request, response)
        } finally {
            log.debug("=== OUTGOING RESPONSE: status {} ===", response.status)
        }
    }
}