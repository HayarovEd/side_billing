package com.impuls.billing.side_billing.model.out_dto.request

data class BlockRequest(
    val vgId: Long,
    val agrmId: Long,
    val changeTime: String,
    val blkReq: Long, //0 - разблокировать, 2 - заблокировать
)
