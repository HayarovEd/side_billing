package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.out_dto.dto.TarifsHistoryDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsStaffDto
import com.impuls.billing.side_billing.utils.SoapParser
import com.impuls.billing.side_billing.utils.handler
import com.impuls.billing.side_billing.utils.toTarifsHistory
import com.impuls.billing.side_billing.utils.toTarifsStaff
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class HomeInternetBillingService(
    private val webClient: WebClient,
) {
    fun getTarifsStaff(
        sessionId: String,
        groupId: Long?, //groupid
        groupTarId: Long?, //grouptarid
        groupModuleId: Long?, //groupmoduleid
        tarId: Long?, //tarid
    ): Mono<List<TarifsStaffDto>> {
        val sGroupId = groupId?.toString() ?: ""
        val sGroupTarIdd = groupTarId?.toString() ?: ""
        val sGroupModuleId = groupModuleId?.toString() ?: ""
        val sTarId = tarId?.toString() ?: ""
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:getTarifsStaff>\n" +
                            "         <filter>\n" +
                            "            <groupid>$sGroupId</groupid>\n" +
                            "            <grouptarid>$sGroupTarIdd</grouptarid>\n" +
                            "            <groupmoduleid>$sGroupModuleId</groupmoduleid>\n" +
                            "            <tarid>$sTarId</tarid>\n" +
                            "         </filter>\n" +
                            "      </urn:getTarifsStaff>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseFullSoapTarifsStaffResponse(body).map { it.toTarifsStaff() }
                }
            }
        )
    }

    fun getTarifsHistory(
        sessionId: String,
    ): Mono<List<TarifsHistoryDto>> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:getTarifsHistory>\n" +
                            "         <flt>\n" +
                            "         </flt>\n" +
                            "      </urn:getTarifsHistory>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseFullSoapTarifsHistoryResponse(body).map { it.toTarifsHistory() }
                }
            }
        )
    }
}