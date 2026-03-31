package com.impuls.billing.side_billing.config

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class LoggingFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val response = exchange.response

        println("=== ВХОДЯЩИЙ ЗАПРОС ===")
        println("Method: ${request.method}")
        println("Path: ${request.path}")
        println("Headers: ${request.headers}")

        return chain.filter(exchange)
            .doOnSuccess {
                println("=== ИСХОДЯЩИЙ ОТВЕТ ===")
                println("Status Code: ${response.statusCode}")
                println("Headers: ${response.headers}")

                // Если статус 401, печатаем стектрейс
                if (response.statusCode == HttpStatus.UNAUTHORIZED) {
                    println("!!! ОТВЕТ 401 UNAUTHORIZED !!!")
                    Thread.currentThread().stackTrace.forEach {
                        println("  at $it")
                    }
                }
            }
            .doOnError { error ->
                println("=== ОШИБКА ===")
                println("Error: ${error.message}")
                error.printStackTrace()
            }
    }
}