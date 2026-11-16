package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.model.out_dto.dto.MonthDayCount
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

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
