package com.impuls.billing.side_billing.security

import com.impuls.billing.side_billing.utils.BEARER_PREF
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Пропускаем CORS preflight OPTIONS запросы
        if ("OPTIONS".equals(request.method, ignoreCase = true)) {
            filterChain.doFilter(request, response)
            return
        }

        val path = request.servletPath

        // Пропускаем публичные пути
        if (path.startsWith("/auth") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-resources") ||
            path.startsWith("/webjars")) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith(BEARER_PREF)) {
            log.warn("Missing or invalid Authorization header for path: {}", path)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("""{"error": "Unauthorized: missing token"}""")
            return
        }

        try {
            if (jwtService.validateAccessToken(authHeader)) {
                val userId = jwtService.getUserIdFromToken(authHeader)
                val authentication = UsernamePasswordAuthenticationToken(userId, null, emptyList())
                SecurityContextHolder.getContext().authentication = authentication
                filterChain.doFilter(request, response)
            } else {
                log.warn("Invalid token for path: {}", path)
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                response.writer.write("""{"error": "Unauthorized: invalid token"}""")
            }
        } catch (e: Exception) {
            log.error("Authentication error for path {}: {}", path, e.message)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("""{"error": "Unauthorized: ${e.message}"}""")
        }
    }
}