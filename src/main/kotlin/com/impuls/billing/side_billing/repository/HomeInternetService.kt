package com.impuls.billing.side_billing.repository

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.in_controller.HomeInternetBillingService
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
    ): Mono<List<TarifsHistoryDto>> {
        val session = tokenService.getSession(authHeader)
        return session?.let {
            monoMapper({
                homeInternetBillingService.getTarifsHistory(
                    sessionId = it.sessionId,
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
}