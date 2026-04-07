package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessLongDto
import com.impuls.billing.side_billing.model.out_dto.request.BlockRequest
import com.impuls.billing.side_billing.repository.BlockService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
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
}