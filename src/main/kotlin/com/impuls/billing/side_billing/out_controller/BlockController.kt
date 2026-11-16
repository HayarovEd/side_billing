package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.client_vgroup.BlockRaspResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessLongDto
import com.impuls.billing.side_billing.model.out_dto.request.BlockRequest
import com.impuls.billing.side_billing.repository.BlockService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/block")
class BlockController(
    private val blockService: BlockService
) {
    @PostMapping("/rasp")
    suspend fun insertBlkRasp(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: BlockRequest
    ): ResultSuccessLongDto {
        return blockService.insertBlkRasp(
            authHeader = authHeader,
            vgId = body.vgId,
            agrmId = body.agrmId,
            changeTime = body.changeTime,
            blkReq = body.blkReq,
        )
    }

    @GetMapping("/rasp")
    suspend fun getBlkRasp(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgid", required = true) vgId: Long,
        @RequestParam(name = "date_from", required = false) dateFrom: String?,
        @RequestParam(name = "date_to", required = false) dateTo: String?,
    ): List<BlockRaspResponse> {
        return blockService.getBlkRasp(
            authHeader = authHeader,
            vgId = vgId,
            dateFrom = dateFrom,
            dateTo = dateTo,
        )
    }
}