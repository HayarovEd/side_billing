package com.impuls.billing.side_billing.model.in_dto.client_account


data class ClientAccountDataDto(
    val application: Int,
    val billdeliveryname: String,
    val account: AccountDto,
    val usergroups: UserGroupsDto,
    val agreements: List<AgreementDto>
)
