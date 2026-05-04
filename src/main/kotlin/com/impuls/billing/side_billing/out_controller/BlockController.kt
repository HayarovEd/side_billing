package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.client_vgroup.BlockRaspResponse
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessLongDto
import com.impuls.billing.side_billing.model.out_dto.request.BlockRequest
import com.impuls.billing.side_billing.repository.BlockService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/block")
class BlockController(
    private val blockService: BlockService
) {
    @PostMapping("/rasp")
    fun insertBlkRasp(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: BlockRequest
    ): Mono<ResultSuccessLongDto> {
        return blockService.insertBlkRasp(
            authHeader = authHeader,
            vgId = body.vgId,
            agrmId = body.agrmId,
            changeTime = body.changeTime,
            blkReq = body.blkReq,
        )
    }

    @GetMapping("/rasp")
    fun getBlkRasp(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgid", required = true) vgId: Long,
        @RequestParam(name = "date_from", required = false) dateFrom: String?,
        @RequestParam(name = "date_to", required = false) dateTo: String?,
    ): Mono<List<BlockRaspResponse>> {
        return blockService.getBlkRasp(
            authHeader = authHeader,
            vgId = vgId,
            dateFrom = dateFrom,
            dateTo = dateTo,
        )
    }
}