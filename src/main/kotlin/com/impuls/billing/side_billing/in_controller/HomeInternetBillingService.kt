package com.impuls.billing.side_billing.in_controller

import com.impuls.billing.side_billing.model.out_dto.dto.TarifFullDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsHistoryDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsStaffDto
import com.impuls.billing.side_billing.utils.SoapParser
import com.impuls.billing.side_billing.utils.handler
import com.impuls.billing.side_billing.utils.toTarifFull
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
        vgId: Long?,
    ): Mono<List<TarifsHistoryDto>> {
        val vgIdString = vgId?.toString() ?: ""
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:getTarifsHistory>\n" +
                            "         <flt>\n" +
                            "         <vgid>$vgIdString</vgid>\n" +
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

    fun getTariff(
        id: Long,
        sessionId: String,
    ): Mono<TarifFullDto> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    " <urn:getTarif>\n" +
                            "         <id>$id</id>\n" +
                            "      </urn:getTarif>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseFullSoapTarifFullResponse(body).toTarifFull()
                }
            }
        )
    }

    fun insertClientTarifsRasp(
        sessionId: String,
        vgId: Long,
        tarIdNew: Long
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:insClientTarifsRasp>\n" +
                            "         <val>\n" +
                            "            <taridnew>$tarIdNew</taridnew>\n" +
                            "           <vgid>$vgId</vgid>\n" +
                            "         </val>\n" +
                            "      </urn:insClientTarifsRasp>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseSetClientInfoResponse(body)
                }
            }
        )
    }

    fun deleteClientTarifsRasp(
        sessionId: String,
        id: Long,
    ): Mono<Long> {
        val soapRequest = buildString {
            append(
                SoapParser.setBaseAppend(
                    "<urn:delClientTarifsRasp>\n" +
                            "         <id>$id</id>\n" +
                            "      </urn:delClientTarifsRasp>\n"
                )
            )
        }
        return webClient.handler(
            soapRequest = soapRequest,
            sessionId = sessionId,
            bodyFunction = { response ->
                response.body?.let { body ->
                    println(body)
                    SoapParser.parseSetClientInfoResponse(body)
                }
            }
        )
    }
}