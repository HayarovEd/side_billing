package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.exception.BillingException
import com.impuls.billing.side_billing.model.out_dto.dto.MonthDayCount
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

fun <T : Any> monoMapper(bodyFunction: () -> Mono<T>): Mono<T> {
    return bodyFunction()
        .switchIfEmpty(Mono.error(BillingException("User not found", HttpStatus.UNAUTHORIZED)))
}


fun <T : Any> WebClient.handler(
    soapRequest: String,
    sessionId: String,
    bodyFunction: (ResponseEntity<String>) -> T?
): Mono<T> {
    return this
        .post()
        .bodyValue(soapRequest)
        .header("Set-Cookie", sessionId)
        .retrieve()
        .toEntity(String::class.java)
        .mapNotNull { response ->
            println(response)
            bodyFunction(response)
        }
        .doOnError {
            println(it)
        }
        .onErrorResume(WebClientResponseException::class.java) { ex ->
            println(ex)
            val faultString = SoapParser.parseFaultString(ex.responseBodyAsString)
            if (faultString?.contains("Invalid login", ignoreCase = true) == true) {
                // Ошибка аутентификации → 401
                Mono.error(BillingException(faultString, HttpStatus.UNAUTHORIZED))
            } else if (!faultString.isNullOrBlank()) {
                Mono.error(BillingException(faultString, HttpStatus.BAD_REQUEST))
                //E-mail for user '$1' is not confirmed
            } else {
                Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Billing service error", ex))
            }
        }
}

suspend fun <T : Any> WebClient.coroutineHandler(
    soapRequest: String,
    sessionId: String,
    bodyFunction: (ResponseEntity<String>) -> T
): T = coroutineScope {

    val response = try {
        this@coroutineHandler
            .post()
            .bodyValue(soapRequest)
            .header("Set-Cookie", sessionId)
            .retrieve()
            .awaitBillingResponse()
    } catch (ex: WebClientResponseException) {
        handleBillingError(ex)
    }

    println(response)
    bodyFunction(response)
}

// Extension для удобной обработки ответа
private suspend fun WebClient.ResponseSpec.awaitBillingResponse(): ResponseEntity<String> {
    return try {
        toEntity(String::class.java).awaitSingle()
    } catch (ex: WebClientResponseException) {
        throw ex // Пробрасываем для обработки выше
    }
}

// Отдельная функция для обработки ошибок биллинга
private fun handleBillingError(ex: WebClientResponseException): Nothing {
    println(ex)
    val faultString = SoapParser.parseFaultString(ex.responseBodyAsString)

    val exception = when {
        faultString?.contains("Invalid login", ignoreCase = true) == true -> {
            BillingException(faultString, HttpStatus.UNAUTHORIZED)
        }
        !faultString.isNullOrBlank() -> {
            BillingException(faultString, HttpStatus.BAD_REQUEST)
        }
        else -> {
            ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Billing service error",
                ex
            )
        }
    }

    throw exception
}

fun convertToDate(dateString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(dateString, formatter)
}

fun parseFlexibleDate(dateString: String): LocalDateTime {
    val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-")
        .appendValue(ChronoField.DAY_OF_MONTH)
        .toFormatter()

    return LocalDate.parse(dateString, formatter).atStartOfDay()
}

fun splitByMonths(
    startDate: LocalDateTime,
    endDate: LocalDateTime
): List<MonthDayCount> {
    val result = mutableListOf<MonthDayCount>()
    var current = startDate.toLocalDate()
    val end = endDate.toLocalDate()

    while (current <= end) {
        val monthStart = current
        val monthEnd = current.withDayOfMonth(current.lengthOfMonth()).takeIf { it <= end } ?: end

        val days = ChronoUnit.DAYS.between(monthStart, monthEnd).toInt() + 1

        result.add(
            MonthDayCount(
                yearMonth = YearMonth.from(monthStart),
                days = days
            )
        )

        current = monthStart.plusMonths(1).withDayOfMonth(1)
    }

    return result
}

fun splitIntervalByMonths(
    start: LocalDateTime,
    end: LocalDateTime
): List<MonthDayCount> {
    val result = mutableListOf<MonthDayCount>()
    var current = start.toLocalDate().withDayOfMonth(1)
    val endDate = end.toLocalDate()

    while (current <= endDate) {
        val monthStart = maxOf(current, start.toLocalDate())
        val monthEnd = minOf(current.withDayOfMonth(current.lengthOfMonth()), endDate)

        val days = ChronoUnit.DAYS.between(monthStart, monthEnd) + 1

        if (days > 0) {
            result.add(
                MonthDayCount(
                    yearMonth = YearMonth.from(current),
                    days = days.toInt()
                )
            )
        }

        current = current.plusMonths(1)
    }

    return result
}
