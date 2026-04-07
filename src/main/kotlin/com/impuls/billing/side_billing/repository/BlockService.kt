package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.BlockBillingService
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessLongDto
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BlockService(
    private val blockBillingService: BlockBillingService,
    private val tokenService: TokenService,
) {
    fun insertBlkRasp(
        authHeader: String,
        vgId: Long,
        agrmId: Long,
        blkReq: Long,
        changeTime: String,
    ): Mono<ResultSuccessLongDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = blockBillingService.insertBlkRasp(
                    sessionId = it.sessionId,
                    vgId = vgId,
                    agrmId = agrmId,
                    changeTime = changeTime,
                    blkReq = blkReq,
                )
                result.map { header ->
                    ResultSuccessLongDto(header)
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }
}