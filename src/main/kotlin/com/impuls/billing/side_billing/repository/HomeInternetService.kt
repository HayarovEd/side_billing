package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.HomeInternetBillingService
import com.impuls.billing.side_billing.model.in_dto.ClientShortStat
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceFull
import com.impuls.billing.side_billing.model.out_dto.dto.AvailableServiceDto
import com.impuls.billing.side_billing.model.out_dto.dto.MacStaffDto
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessDto
import com.impuls.billing.side_billing.model.out_dto.dto.ResultSuccessLongDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifFullDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsHistoryDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsStaffDto
import com.impuls.billing.side_billing.utils.monoMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class HomeInternetService(
    private val tokenService: TokenService,
    private val homeInternetBillingService: HomeInternetBillingService
) {

    fun getTarifsStaff(
        authHeader: String,
        groupId: Long?,
        groupTarId: Long?,
        groupModuleId: Long?,
        tarId: Long?,
    ): Mono<List<TarifsStaffDto>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getTarifsStaff(
                    sessionId = it.sessionId,
                    groupId = groupId,
                    groupTarId = groupTarId,
                    groupModuleId = groupModuleId,
                    tarId = tarId,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getTarifsHistory(
        authHeader: String,
        vgId: Long?,
    ): Mono<List<TarifsHistoryDto>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getTarifsHistory(
                    sessionId = it.sessionId,
                    vgId = vgId,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getTariff(
        authHeader: String,
        id: Long,
    ): Mono<TarifFullDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getTariff(
                    sessionId = it.sessionId,
                    id = id,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun insertClientTarifsRasp(
        authHeader: String,
        vgId: Long,
        tarIdNew: Long
    ): Mono<Long> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.insertClientTarifsRasp(
                    sessionId = it.sessionId,
                    vgId = vgId,
                    tarIdNew = tarIdNew
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun deleteClientTarifsRasp(
        authHeader: String,
        id: Long,
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = homeInternetBillingService.deleteClientTarifsRasp(
                    sessionId = it.sessionId,
                    id = id
                )
                result.map { header ->
                    ResultSuccessDto(header == 1L)
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getMacStaff(
        authHeader: String,
        id: Long,
    ): Mono<List<MacStaffDto>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getMacStaff(
                    sessionId = it.sessionId,
                    id = id
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }


    fun insertMacStaff(
        authHeader: String,
        id: Long,
        mac: String,
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = homeInternetBillingService.insertMacStaff(
                    sessionId = it.sessionId,
                    id = id,
                    mac = mac
                )
                result.map { header ->
                    ResultSuccessDto(header == 1L)
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getAvailableServicesForVgroup(
        authHeader: String,
        vgId: Long,
    ): Mono<List<AvailableServiceDto>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getAvailableServicesForVgroup(
                    sessionId = it.sessionId,
                    vgId = vgId
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun insertClientUsboxService(
        authHeader: String,
        vgId: Long,
        tarId: Long,
        catIdx: Long,
        mui: Double,
        notusBox: Long,
    ): Mono<ResultSuccessLongDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = homeInternetBillingService.insertClientUsboxService(
                    sessionId = it.sessionId,
                    vgId = vgId,
                    tarId = tarId,
                    catIdx = catIdx,
                    mui = mui,
                    notusBox = notusBox
                )
                result.map { header ->
                    ResultSuccessLongDto(header)
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getUsboxServices(
        authHeader: String,
        vgId: Long,
    ): Mono<List<UsboxServiceFull>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getUsboxServices(
                    sessionId = it.sessionId,
                    vgId = vgId
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }


    fun deleteUsboxService(
        authHeader: String,
        servId: Long,
        notusBox: Long,
    ): Mono<ResultSuccessDto> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                val result = homeInternetBillingService.deleteUsboxService(
                    sessionId = it.sessionId,
                    servId = servId,
                    notusBox = notusBox
                )
                result.map { header ->
                    ResultSuccessDto(header == 1L)
                }
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }

    fun getClientStatByTimePeriod(
        authHeader: String,
        vgId: Long,
        repDetail: Long,
        dtFrom: String,
        dtTo: String,
    ): Mono<List<ClientShortStat>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getClientStatByTimePeriod(
                    sessionId = it.sessionId,
                    vgId = vgId,
                    dtFrom = dtFrom,
                    dtTo = dtTo,
                    repDetail = repDetail,
                )
            })
        } ?: throw BillingException("Session not found", HttpStatus.UNAUTHORIZED)
    }


}