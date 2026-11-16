package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.ClientIpStat
import com.impuls.billing.side_billing.model.in_dto.ClientShortStat
import com.impuls.billing.side_billing.model.in_dto.ClientTarifStat
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceFull
import com.impuls.billing.side_billing.model.out_dto.dto.*
import com.impuls.billing.side_billing.model.out_dto.request.MacRequest
import com.impuls.billing.side_billing.model.out_dto.request.RaspRequest
import com.impuls.billing.side_billing.model.out_dto.request.UsboxServiceRequest
import com.impuls.billing.side_billing.repository.HomeInternetService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/home")
class HomeInternetController(
    private val homeInternetService: HomeInternetService
) {

    @GetMapping("/tariff")
    suspend fun getTarifsStaff(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "groupid", required = false) groupId: Long?,
        @RequestParam(name = "grouptarid", required = false) groupTarId: Long?,
        @RequestParam(name = "groupmoduleid", required = false) groupModuleId: Long?,
        @RequestParam(name = "tarid", required = false) tarId: Long?,
    ): List<TarifsStaffDto> {
        return homeInternetService.getTarifsStaff(
            authHeader = authHeader,
            groupId = groupId,
            groupTarId = groupTarId,
            groupModuleId = groupModuleId,
            tarId = tarId,
        )
    }

    @GetMapping("/tariff/history")
    suspend fun getTarifsHistory(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgid", required = false) vgId: Long?,
    ): List<TarifsHistoryDto> {
        return homeInternetService.getTarifsHistory(
            authHeader = authHeader,
            vgId = vgId,
        )
    }

    @GetMapping("/tariff/{id}")
    suspend fun getTariff(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable("id") id: Long,
    ): TarifFullDto {
        return homeInternetService.getTariff(
            authHeader = authHeader,
            id = id
        )
    }

    @PostMapping("/tariff/rasp")
    suspend fun insertClientTarifsRasp(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: RaspRequest
    ): Long {
        return homeInternetService.insertClientTarifsRasp(
            authHeader = authHeader,
            vgId = body.vgId,
            tarIdNew = body.tarIdNew
        )
    }

    @DeleteMapping("/tariff/rasp/{id}")
    suspend fun deleteClientTarifsRasp(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable("id") id: Long,
    ): ResultSuccessDto {
        return homeInternetService.deleteClientTarifsRasp(
            authHeader = authHeader,
            id = id
        )
    }

    @GetMapping("/mac")
    suspend fun getMacStaff(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "id", required = true) id: Long,
    ): List<MacStaffDto> {
        return homeInternetService.getMacStaff(
            authHeader = authHeader,
            id = id,
        )
    }

    @PostMapping("/mac")
    suspend fun insertMacStaff(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: MacRequest
    ): ResultSuccessDto {
        return homeInternetService.insertMacStaff(
            authHeader = authHeader,
            id = body.id,
            mac = body.mac,
        )
    }

    @GetMapping("/availableService")
    suspend fun getAvailableServicesForVgroup(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
    ): List<AvailableServiceDto> {
        return homeInternetService.getAvailableServicesForVgroup(
            authHeader = authHeader,
            vgId = vgId,
        )
    }

    @PostMapping("/service")
    suspend fun insertClientUsboxService(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody body: UsboxServiceRequest
    ): ResultSuccessLongDto {
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
    suspend fun getUsboxServices(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
    ): List<UsboxServiceFull> {
        return homeInternetService.getUsboxServices(
            authHeader = authHeader,
            vgId = vgId,
        )
    }

    @DeleteMapping("/service/{id}")
    suspend fun deleteUsboxService(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable("id") id: Long,
    ): ResultSuccessDto {
        return homeInternetService.deleteUsboxService(
            authHeader = authHeader,
            servId = id,
            notusBox = 1L
        )
    }

    @GetMapping("/short_statistics")
    suspend fun getClientStatByTimePeriod(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
        @RequestParam(name = "repDetail", required = true) repDetail: Long,
        @RequestParam(name = "dtFrom", required = true) dtFrom: String,
        @RequestParam(name = "dtTo", required = true) dtTo: String,
    ): List<ClientShortStat> {
        return homeInternetService.getClientStatByTimePeriod(
            authHeader = authHeader,
            vgId = vgId,
            repDetail = repDetail,
            dtFrom = dtFrom,
            dtTo = dtTo
        )
    }

    @GetMapping("/tarifStatistics")
    suspend fun getClientTarifStatByTimePeriod(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
        @RequestParam(name = "dtFrom", required = true) dtFrom: String,
        @RequestParam(name = "dtTo", required = true) dtTo: String,
    ): List<ClientTarifStat> {
        return homeInternetService.getClientTarifStatByTimePeriod(
            authHeader = authHeader,
            vgId = vgId,
            repDetail = 1,
            dtFrom = dtFrom,
            dtTo = dtTo
        )
    }

    @GetMapping("/ipStatistics")
    suspend fun getClientIpStatByTimePeriod(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "vgId", required = true) vgId: Long,
        @RequestParam(name = "dtFrom", required = true) dtFrom: String,
        @RequestParam(name = "dtTo", required = true) dtTo: String,
    ): List<ClientIpStat> {
        return homeInternetService.getClientIpStatByTimePeriod(
            authHeader = authHeader,
            vgId = vgId,
            repDetail = 1,
            dtFrom = dtFrom,
            dtTo = dtTo
        )
    }
}