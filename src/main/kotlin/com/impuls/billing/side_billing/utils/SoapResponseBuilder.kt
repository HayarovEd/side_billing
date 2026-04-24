package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.model.in_dto.notices.AccountNoticeResponse
import org.jsoup.nodes.Document

object SoapResponseBuilder {
    fun buildSoapAccountNoticeResponse(response: AccountNoticeResponse): String {
        // Создаем XML документ с правильной декларацией
        val doc = Document.createShell("")
        doc.outputSettings().prettyPrint(true)

        // Создаем SOAP-ENV:Envelope
        val envelope = doc.appendElement("SOAP-ENV:Envelope")
        envelope.attr("xmlns:SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/")
        envelope.attr("xmlns:SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/")
        envelope.attr("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
        envelope.attr("xmlns:xsd", "http://www.w3.org/2001/XMLSchema")
        envelope.attr("xmlns:lbapi", "urn:api3")

        // Добавляем Header
        envelope.appendElement("SOAP-ENV:Header")

        // Добавляем Body
        val body = envelope.appendElement("SOAP-ENV:Body")

        // Добавляем getAccountNoticesResponse
        val responseElement = body.appendElement("urn:setAccountNotices")

        // Добавляем ret
        val ret = responseElement.appendElement("an")

        // Добавляем uid
        ret.appendElement("uid").text(response.uid.toString())

        // Добавляем все notices
        response.notices.forEach { notice ->
            val noticesElement = ret.appendElement("notices")
            noticesElement.appendElement("type").text(notice.type.toString())
            noticesElement.appendElement("isemail").text(if (notice.isEmail)"1" else "0")
            noticesElement.appendElement("issms").text(if (notice.isSms)"1" else "0")
            noticesElement.appendElement("ismessenger").text(if (notice.isMessenger)"1" else "0")
            noticesElement.appendElement("name").text(notice.name)
            noticesElement.appendElement("descr").text(notice.descr)
            noticesElement.appendElement("value").text(notice.value)
        }

        // Возвращаем XML как строку
        return doc.outerHtml()
    }
}