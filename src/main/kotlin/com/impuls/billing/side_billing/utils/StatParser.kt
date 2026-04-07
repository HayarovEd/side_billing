package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.model.in_dto.ClientShortStat
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

object StatParser {
    fun parseClientShortStat(xmlString: String): List<ClientShortStat> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        val stats = mutableListOf<ClientShortStat>()
        retElements.forEach { retElement ->
            val values = retElement.select("data").map { it.text() }
            if (values.size >= 15) {
                stats.add(
                    ClientShortStat(
                        agrmId = values[0].toLongOrNull(),
                        vgId = values[1].toLongOrNull(),
                        agentId = values[2].toLongOrNull(),
                        agentType = values[3].toLongOrNull(),
                        contIn = values[4].toLongOrNull(),
                        cntOut = values[5].toLongOrNull(),
                        duration = values[6].toLongOrNull(),
                        amount = values[7].toDoubleOrNull(),
                        dt = values[8],
                        agentDescr = values[9],
                        currSymbol = values[10],
                        volume = values[11].toDoubleOrNull(),
                        volumeIn = values[12].toDoubleOrNull(),
                        volumeOut = values[13].toDoubleOrNull(),
                        total = values[14].toLongOrNull(),
                    )
                )
            }
        }
        return stats
    }

}