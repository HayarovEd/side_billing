package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.ClientIpStat
import com.impuls.billing.side_billing.model.in_dto.ClientShortStat
import com.impuls.billing.side_billing.model.in_dto.ClientTarifStat
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceFull
import com.impuls.billing.side_billing.model.out_dto.dto.AvailableServiceDto
import com.impuls.billing.side_billing.model.out_dto.dto.MacStaffDto
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessLongDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifFullDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsHistoryDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsStaffDto
import com.impuls.billing.side_billing.model.out_dto.request.MacRequest
import com.impuls.billing.side_billing.model.out_dto.request.RaspRequest
import com.impuls.billing.side_billing.model.out_dto.request.UsboxServiceRequest
import com.impuls.billing.side_billing.repository.HomeInternetService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
        @RequestParam(name = "vgid", required = false) vgId: Long?,
    ): Mono<List<TarifsHistoryDto>> {
        return homeInternetService.getTarifsHistory(
            authHeader = authHeader,
            vgId = vgId,
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

    @PostMapping("/tariff/rasp")
    fun insertClientTarifsRasp(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: RaspRequest
    ): Mono<Long> {
        return homeInternetService.insertClientTarifsRasp(
            authHeader = authHeader,
            vgId = body.vgId,
            tarIdNew = body.tarIdNew
        )
    }

    @DeleteMapping("/tariff/rasp/{id}")
    fun deleteClientTarifsRasp(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable("id") id: Long,
    ): Mono<ResultSuccessDto> {
        return homeInternetService.deleteClientTarifsRasp(
            authHeader = authHeader,
            id = id
        )
    }

    @GetMapping("/mac")
    fun getMacStaff(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "id", required = true) id: Long,
    ): Mono<List<MacStaffDto>> {
        return homeInternetService.getMacStaff(
            authHeader = authHeader,
            id = id,
        )
    }


    @PostMapping("/mac")
    fun insertMacStaff(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: MacRequest
    ): Mono<ResultSuccessDto> {
        return homeInternetService.insertMacStaff(
            authHeader = authHeader,
            id = body.id,
            mac = body.mac,
        )
    }

    @GetMapping("/availableService")
    fun getAvailableServicesForVgroup(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
    ): Mono<List<AvailableServiceDto>> {
        return homeInternetService.getAvailableServicesForVgroup(
            authHeader = authHeader,
            vgId = vgId,
        )
    }

    @PostMapping("/service")
    fun insertClientUsboxService(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: UsboxServiceRequest
    ): Mono<ResultSuccessLongDto> {
        return homeInternetService.insertClientUsboxService(
            authHeader = authHeader,
            vgId = body.vgId,
            tarId = body.tarId,
            catIdx = body.catIdx,
            mui = body.mui,
            notusBox = body.notusBox
        )
    }

    @GetMapping("/service")
    fun getUsboxServices(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
    ): Mono<List<UsboxServiceFull>> {
        return homeInternetService.getUsboxServices(
            authHeader = authHeader,
            vgId = vgId,
        )
    }

    @DeleteMapping("/service/{id}")
    fun deleteUsboxService(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable("id") id: Long,
    ): Mono<ResultSuccessDto> {
        return homeInternetService.deleteUsboxService(
            authHeader = authHeader,
            servId = id,
            notusBox = 1L
        )
    }

    @GetMapping("/short_statistics")
    fun getClientStatByTimePeriod(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
        @RequestParam(name = "repDetail", required = true) repDetail: Long, //Вид детализации (единица времени): 1 — по дням, 2 — по часам, 3— по месяцам, 0 — весь период
        @RequestParam(name = "dtFrom", required = true) dtFrom: String,
        @RequestParam(name = "dtTo", required = true) dtTo: String,
    ): Mono<List<ClientShortStat>> {
        return homeInternetService.getClientStatByTimePeriod(
            authHeader = authHeader,
            vgId = vgId,
            repDetail = repDetail,
            dtFrom = dtFrom,
            dtTo = dtTo
        )
    }

    @GetMapping("/tarifStatistics")
    fun getClientTarifStatByTimePeriod(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
        @RequestParam(name = "dtFrom", required = true) dtFrom: String,
        @RequestParam(name = "dtTo", required = true) dtTo: String,
    ): Mono<List<ClientTarifStat>> {
        return homeInternetService.getClientTarifStatByTimePeriod(
            authHeader = authHeader,
            vgId = vgId,
            repDetail = 1,
            dtFrom = dtFrom,
            dtTo = dtTo
        )
    }

    @GetMapping("/ipStatistics")
    fun getClientIpStatByTimePeriod(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
        @RequestParam(name = "dtFrom", required = true) dtFrom: String,
        @RequestParam(name = "dtTo", required = true) dtTo: String,
    ): Mono<List<ClientIpStat>> {
        return homeInternetService.getClientIpStatByTimePeriod(
            authHeader = authHeader,
            vgId = vgId,
            repDetail = 1,
            dtFrom = dtFrom,
            dtTo = dtTo
        )
    }

}