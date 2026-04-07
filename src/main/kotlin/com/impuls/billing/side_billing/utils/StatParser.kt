package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.model.in_dto.ClientIpStat
import com.impuls.billing.side_billing.model.in_dto.ClientShortStat
import com.impuls.billing.side_billing.model.in_dto.ClientTarifStat
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

    fun parseClientTarifStat(xmlString: String): List<ClientTarifStat> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        val stats = mutableListOf<ClientTarifStat>()
        retElements.forEach { retElement ->
            val values = retElement.select("data").map { it.text() }
            if (values.size >= 13) {
                stats.add(
                    ClientTarifStat(
                        agrmId = values[0].toLongOrNull(),
                        vgId = values[1].toLongOrNull(),
                        agentId = values[2].toLongOrNull(),
                        agentType = values[3].toLongOrNull(),
                        agentDescr = values[4],
                        zoneDescr = values[5],
                        currSymbol = values[6],
                        tarId = values[7].toLongOrNull(),
                        catIdx = values[8].toLongOrNull(),
                        volume = values[9].toDoubleOrNull(),
                        volumeIn = values[10].toDoubleOrNull(),
                        volumeOut = values[11].toDoubleOrNull(),
                        amount = values[12].toDoubleOrNull(),
                    )
                )
            }
        }
        return stats
    }

    fun parseClientIpStat(xmlString: String): List<ClientIpStat> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        val stats = mutableListOf<ClientIpStat>()
        retElements.forEach { retElement ->
            val values = retElement.select("data").map { it.text() }
            if (values.size >= 11) {
                stats.add(
                    ClientIpStat(
                        agrmId = values[0].toLongOrNull(),
                        vgId = values[1].toLongOrNull(),
                        agentId = values[2].toLongOrNull(),
                        agentType = values[3].toLongOrNull(),
                        agentDescr = values[4],
                        currSymbol = values[5],
                        srcIp = values[6],
                        volume = values[7].toDoubleOrNull(),
                        volumeIn = values[8].toDoubleOrNull(),
                        volumeOut = values[9].toDoubleOrNull(),
                        amount = values[10].toDoubleOrNull(),
                    )
                )
            }
        }
        return stats
    }

}