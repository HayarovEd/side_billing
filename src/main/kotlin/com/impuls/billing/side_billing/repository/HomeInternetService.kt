package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.model.in_dto.ClientIpStat
import com.impuls.billing.side_billing.model.in_dto.ClientShortStat
import com.impuls.billing.side_billing.model.in_dto.ClientTarifStat
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceFull
import com.impuls.billing.side_billing.model.out_dto.dto.*
import com.impuls.billing.side_billing.soap.LanBillingClient
import com.impuls.billing.side_billing.utils.*
import com.impuls.billing.soap.generated.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class HomeInternetService(
    private val tokenService: TokenService,
    private val billingClient: LanBillingClient,
) {

    private fun getSessionOrThrow(authHeader: String): String {
        return tokenService.getSession(authHeader)?.sessionId
            ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    suspend fun getTarifsStaff(
        authHeader: String,
        groupId: Long?,
        groupTarId: Long?,
        groupModuleId: Long?,
        tarId: Long?,
    ): List<TarifsStaffDto> {
        val sessionId = getSessionOrThrow(authHeader)
        val filter = SoapTarifsStaff().apply {
            if (groupId != null) this.groupid = groupId
            if (groupTarId != null) this.grouptarid = groupTarId
            if (groupModuleId != null) this.groupmoduleid = groupModuleId
            if (tarId != null) this.tarid = tarId
        }
        val list = billingClient.execute(sessionId) {
            getTarifsStaff(filter)
        }
        return list.map { it.toTarifsStaff() }
    }

    suspend fun getTarifsHistory(
        authHeader: String,
        vgId: Long?,
    ): List<TarifsHistoryDto> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            if (vgId != null) this.vgid = vgId
        }
        val list = billingClient.execute(sessionId) {
            getTarifsHistory(flt)
        }
        return list.map { it.toTarifsHistory() }
    }

    suspend fun getTariff(
        authHeader: String,
        id: Long,
    ): TarifFullDto {
        val sessionId = getSessionOrThrow(authHeader)
        val list = billingClient.execute(sessionId) {
            getTarif(id)
        }
        val first = list.firstOrNull()
            ?: throw BillingException("Tariff not found", HttpStatus.NOT_FOUND)
        return first.toTarifFull()
    }

    suspend fun insertClientTarifsRasp(
        authHeader: String,
        vgId: Long,
        tarIdNew: Long
    ): Long {
        val sessionId = getSessionOrThrow(authHeader)
        val valRasp = SoapTarifsRasp().apply {
            this.vgid = vgId
            this.taridnew = tarIdNew
        }
        return billingClient.execute(sessionId) {
            insClientTarifsRasp(valRasp)
        }
    }

    suspend fun deleteClientTarifsRasp(
        authHeader: String,
        id: Long,
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            delClientTarifsRasp(id)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun getMacStaff(
        authHeader: String,
        id: Long,
    ): List<MacStaffDto> {
        val sessionId = getSessionOrThrow(authHeader)
        val list = billingClient.execute(sessionId) {
            getMacStaff(SoapFilter(), id)
        }
        return list.map { it.toMacStaff() }
    }

    suspend fun insertMacStaff(
        authHeader: String,
        id: Long,
        mac: String,
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val valMac = SoapMacStaff().apply {
            this.vgid = id
            this.mac = mac
        }
        val res = billingClient.execute(sessionId) {
            insupdMacStaff(1L, valMac)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun getAvailableServicesForVgroup(
        authHeader: String,
        vgId: Long,
    ): List<AvailableServiceDto> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = AvailableServicesFilter().apply {
            this.vgid = vgId
        }
        val list = billingClient.execute(sessionId) {
            getAvailableServicesForVgroup(flt)
        }
        return list.map { it.toAvailableService() }
    }

    suspend fun insertClientUsboxService(
        authHeader: String,
        vgId: Long,
        tarId: Long,
        catIdx: Long,
        mui: Double,
        notusBox: Long,
    ): ResultSuccessLongDto {
        val sessionId = getSessionOrThrow(authHeader)
        val valServ = SoapUsboxService().apply {
            this.vgid = vgId
            this.tarid = tarId
            this.catidx = catIdx
            this.mul = mui
            this.notusbox = notusBox
        }
        val res = billingClient.execute(sessionId) {
            insupdClientUsboxService(1L, valServ)
        }
        return ResultSuccessLongDto(res)
    }

    suspend fun getUsboxServices(
        authHeader: String,
        vgId: Long,
    ): List<UsboxServiceFull> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            this.vgid = vgId
        }
        val list = billingClient.execute(sessionId) {
            getUsboxServices(flt, emptyList())
        }
        return list.map { it.toUsboxServiceFull() }
    }

    suspend fun deleteUsboxService(
        authHeader: String,
        servId: Long,
        notusBox: Long,
    ): ResultSuccessDto {
        val sessionId = getSessionOrThrow(authHeader)
        val res = billingClient.execute(sessionId) {
            delUsboxService(servId, notusBox, 0L)
        }
        return ResultSuccessDto(res == 1L)
    }

    suspend fun getClientStatByTimePeriod(
        authHeader: String,
        vgId: Long,
        repDetail: Long,
        dtFrom: String,
        dtTo: String,
    ): List<ClientShortStat> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            this.repnum = 3L
            this.vgid = vgId
            this.repdetail = repDetail
            this.dtfrom = dtFrom
            this.dtto = dtTo
        }
        val statList = billingClient.execute(sessionId) {
            getClientStat(flt, emptyList())
        }
        return statList.firstOrNull()?.toClientShortStatList() ?: emptyList()
    }

    suspend fun getClientTarifStatByTimePeriod(
        authHeader: String,
        vgId: Long,
        repDetail: Long,
        dtFrom: String,
        dtTo: String,
    ): List<ClientTarifStat> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            this.repnum = 3L
            this.vgid = vgId
            this.repdetail = repDetail
            this.dtfrom = dtFrom
            this.dtto = dtTo
        }
        val statList = billingClient.execute(sessionId) {
            getClientStat(flt, emptyList())
        }
        return statList.firstOrNull()?.toClientTarifStatList() ?: emptyList()
    }

    suspend fun getClientIpStatByTimePeriod(
        authHeader: String,
        vgId: Long,
        repDetail: Long,
        dtFrom: String,
        dtTo: String,
    ): List<ClientIpStat> {
        val sessionId = getSessionOrThrow(authHeader)
        val flt = SoapFilter().apply {
            this.repnum = 3L
            this.vgid = vgId
            this.repdetail = repDetail
            this.dtfrom = dtFrom
            this.dtto = dtTo
        }
        val statList = billingClient.execute(sessionId) {
            getClientStat(flt, emptyList())
        }
        return statList.firstOrNull()?.toClientIpStatList() ?: emptyList()
    }
}