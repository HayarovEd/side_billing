package com.impuls.billing.side_billing.model.in_dto.notices

data class AccountNoticeResponse(
    val uid: Long,
    val notices: List<NoticeResponse>
)
