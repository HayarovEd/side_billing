package com.impuls.billing.side_billing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SideBillingApplication

fun main(args: Array<String>) {
	runApplication<SideBillingApplication>(*args)
}
