package com.impuls.billing.side_billing.model.out_dto.dto

import java.time.YearMonth

data class MonthDayCount(
    val yearMonth: YearMonth,  // Год и месяц
    val days: Int
)