package com.impuls.billing.side_billing.soap

import com.impuls.billing.soap.generated.Api3PortType
import jakarta.xml.ws.BindingProvider
import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.transport.http.HTTPConduit
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SoapClientConfig {

    @Value("\${base.url}")
    private lateinit var baseUrl: String

    @Bean
    fun api3PortType(): Api3PortType {
        val factory = JaxWsProxyFactoryBean().apply {
            serviceClass = Api3PortType::class.java
            address = baseUrl
        }
        val port = factory.create(Api3PortType::class.java)

        (port as BindingProvider).requestContext["thread.local.request.context"] = "true"

        val client: Client = ClientProxy.getClient(port)
        val conduit = client.conduit as HTTPConduit
        val policy = HTTPClientPolicy().apply {
            connectionTimeout = 10000L
            receiveTimeout = 30000L
            isAllowChunking = false
        }
        conduit.client = policy

        return port
    }
}
