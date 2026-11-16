package com.impuls.billing.side_billing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SideBillingApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SideBillingApplication>(*args)
        }
    }
}

fun main(args: Array<String>) {
    SideBillingApplication.main(args)
}
