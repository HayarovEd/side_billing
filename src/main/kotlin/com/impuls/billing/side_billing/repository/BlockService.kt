package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.BlockRaspResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessLongDto
import com.impuls.billing.side_billing.soap.LanBillingClient
import com.impuls.billing.side_billing.utils.toBlockRaspResponse
import com.impuls.billing.soap.generated.SoapBlockRasp
import com.impuls.billing.soap.generated.SoapFilter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class BlockService(
    private val billingClient: LanBillingClient,
    private val tokenService: TokenService,
) {
    suspend fun insertBlkRasp(
        authHeader: String,
        vgId: Long,
        agrmId: Long,
        blkReq: Long,
        changeTime: String,
    ): ResultSuccessLongDto {
        val sessionId = tokenService.getSession(authHeader)?.sessionId
            ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)

        val valBlk = SoapBlockRasp().apply {
            this.vgid = vgId
            this.agrmid = agrmId
            this.changetime = changeTime
            this.blkreq = blkReq
        }
        val res = billingClient.execute(sessionId) {
            insBlkRasp(valBlk)
        }
        return ResultSuccessLongDto(res)
    }

    suspend fun getBlkRasp(
        authHeader: String,
        vgId: Long,
        dateFrom: String?,
        dateTo: String?,
    ): List<BlockRaspResponse> {
        val sessionId = tokenService.getSession(authHeader)?.sessionId
            ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)

        val flt = SoapFilter().apply {
            this.vgid = vgId
            if (!dateFrom.isNullOrEmpty()) this.dtfrom = dateFrom
            if (!dateTo.isNullOrEmpty()) this.dtto = dateTo
        }
        val list = billingClient.execute(sessionId) {
            getBlockRasp(flt, emptyList())
        }
        return list.map { it.toBlockRaspResponse() }
    }
}