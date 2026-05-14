package com.impuls.billing.side_billing.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Value("\${base.url}")
    private lateinit var baseUrl: String

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "text/xml; charset=utf-8")
            .build()
    }
}