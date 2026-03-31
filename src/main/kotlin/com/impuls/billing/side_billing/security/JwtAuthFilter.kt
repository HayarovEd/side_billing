package com.impuls.billing.side_billing.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // Пропускаем публичные пути
        if (path.startsWith("/auth")) {
            println("Публичный путь: $path, пропускаем")
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")
        println("Проверка токена для: $path")

        if (authHeader == null || !authHeader.startsWith(BEARER_PREF)) {
            println("Отсутствует Authorization header")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        try {
            if (jwtService.validateAccessToken(authHeader)) {
                val userId = jwtService.getUserIdFromToken(authHeader)
                val authentication = UsernamePasswordAuthenticationToken(userId, null, emptyList())

                // Устанавливаем в SecurityContextHolder
                SecurityContextHolder.getContext().authentication = authentication

                println("Аутентификация установлена для: $userId")
                filterChain.doFilter(request, response)
            } else {
                println("Невалидный токен")
                response.status = HttpServletResponse.SC_UNAUTHORIZED
            }
        } catch (e: Exception) {
            println("Ошибка: ${e.message}")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
        }
    }
}