package com.impuls.billing.side_billing.config

import com.impuls.billing.side_billing.utils.BASE_INNER_URL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl(BASE_INNER_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "text/xml; charset=utf-8")
            .build()
    }
}