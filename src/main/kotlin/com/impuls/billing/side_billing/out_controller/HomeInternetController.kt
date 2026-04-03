package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.out_dto.dto.TarifFullDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsHistoryDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsStaffDto
import com.impuls.billing.side_billing.repository.HomeInternetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/home")
class HomeInternetController(
    private val homeInternetService: HomeInternetService
) {

    @GetMapping("/tariff")
    fun getTarifsStaff(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "groupid", required = false) groupId: Long?,
        @RequestParam(name = "grouptarid", required = false) groupTarId: Long?,
        @RequestParam(name = "groupmoduleid", required = false) groupModuleId: Long?,
        @RequestParam(name = "tarid", required = false) tarId: Long?,
    ): Mono<List<TarifsStaffDto>> {
        return homeInternetService.getTarifsStaff(
            authHeader = authHeader,
            groupId = groupId,
            groupTarId = groupTarId,
            groupModuleId = groupModuleId,
            tarId = tarId,
        )
    }

    @GetMapping("/tariff/history")
    fun getTarifsHistory(
        @RequestHeader("Authorization") authHeader: String,
    ): Mono<List<TarifsHistoryDto>> {
        return homeInternetService.getTarifsHistory(
            authHeader = authHeader,
        )
    }

    @GetMapping("/tariff/{id}")
    fun getTariff(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable("id") id: Long,
    ): Mono<TarifFullDto> {
        return homeInternetService.getTariff(
            authHeader = authHeader,
            id = id
        )
    }
}