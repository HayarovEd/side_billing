package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.model.in_dto.AvailableServiceResponse
import com.impuls.billing.side_billing.model.in_dto.ClientInfoResponse
import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.in_dto.PPSettingsResponse
import com.impuls.billing.side_billing.model.in_dto.PasswordResponse
import com.impuls.billing.side_billing.model.in_dto.PromisePaymentResponse
import com.impuls.billing.side_billing.model.in_dto.RecoverPasswordResponse
import com.impuls.billing.side_billing.model.in_dto.SharedPostResponse
import com.impuls.billing.side_billing.model.in_dto.TarifsHistoryResponse
import com.impuls.billing.side_billing.model.in_dto.TarifsStaffResponse
import com.impuls.billing.side_billing.model.in_dto.client_account.AccountDto
import com.impuls.billing.side_billing.model.in_dto.client_account.AgreementDto
import com.impuls.billing.side_billing.model.in_dto.client_account.ClientAccountDataDto
import com.impuls.billing.side_billing.model.in_dto.client_account.GetClientAccountResponse
import com.impuls.billing.side_billing.model.in_dto.client_account.UserGroupDto
import com.impuls.billing.side_billing.model.in_dto.client_account.UserGroupsDto
import com.impuls.billing.side_billing.model.in_dto.client_payment.ClientPaymentResponse
import com.impuls.billing.side_billing.model.in_dto.client_payment.ClientPaymentsFullResponse
import com.impuls.billing.side_billing.model.in_dto.client_payment.PaymentOrderIdNameResponse
import com.impuls.billing.side_billing.model.in_dto.client_payment.PaymentResponse
import com.impuls.billing.side_billing.model.in_dto.client_payment.UprsPaymentResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.AddressBriefResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.BlockRaspResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.CategoryDiscountResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.ClientVgroupResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.CurrentModifierResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.IPMaskResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.MacStaffResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.StaffResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.TariffsStaffResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.TarifsRaspResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.TurboShapeResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.VgroupAddonResponse
import com.impuls.billing.side_billing.model.in_dto.notices.AccountNoticeResponse
import com.impuls.billing.side_billing.model.in_dto.notices.NoticeResponse
import com.impuls.billing.side_billing.model.in_dto.tariff.TarifFullResponse
import com.impuls.billing.side_billing.model.in_dto.tariff.TarifResponse
import com.impuls.billing.side_billing.model.in_dto.tariff.TariffsSettingsRentAsServiceResponse
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceFull
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceResponse
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.jsoup.select.Elements

object SoapParser {

    fun parseBlockRasp(xmlString: String): List<BlockRaspResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        return retElements.map { retElement->
            BlockRaspResponse(
                recordId = retElement.select("recordid").text().toLongOrNull()?:0L,
                vgId = retElement.select("vgid").text().toLongOrNull()?:0L,
                groupId = retElement.select("groupid").text().toLongOrNull()?:0L,
                blkReq = retElement.select("blkreq").text().toLongOrNull()?:0L,
                id = retElement.select("id").text().toLongOrNull()?:0L,
                isFreewill = retElement.select("isfreewill").text().toLongOrNull()==1L,
                requestBy = retElement.select("requestby").text().toLongOrNull()?:0L,
                unBlockedBy = retElement.select("unblockedby").text().toLongOrNull()?:0L,
                agrmId = retElement.select("agrmid").text().toLongOrNull()?:0L,
                uid = retElement.select("uid").text().toLongOrNull()?:0L,
                changeTime = retElement.select("changetime").text(),
                mgrName = retElement.select("mgrname").text(),
                mgrDescr = retElement.select("mgrdescr").text(),
                comment = retElement.select("comment").text(),
                timeTo = retElement.select("timeto").text(),
                login = retElement.select("login").text(),
                unBlockedManagerLogin = retElement.select("unblockedmanagerlogin").text(),
                unBlockedManagerName = retElement.select("unblockedmanagername").text(),
                unBlockedManagerDescr = retElement.select("unblockedmanagerdescr").text(),
                agrmNum = retElement.select("agrmnum").text(),
                username = retElement.select("username").text()
            )
        }
    }


    fun parseClientInfo(xmlString: String): ClientInfoResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret")
        return ClientInfoResponse(
            mobileIsConfirmed = retElement.select("mobileisconfirmed").text().toBoolean(),
            emailIsConfirmed = retElement.select("emailisconfirmed").text().toBoolean(),
            offerIsAccepted = retElement.select("offerisaccepted").text().toBoolean(),
            passIsTemporary = retElement.select("passistemporary").text().toBoolean(),
            triggerContactDataReminder = retElement.select("triggercontactdatareminder").text().toBoolean(),
            email = retElement.select("email").text(),
            password = retElement.select("pass").text(),
            phone = retElement.select("phone").text(),
            kontPerson = retElement.select("kontperson").text(),
            mobile = retElement.select("mobile").text(),
        )
    }

    fun parseAccountNotices(xmlString: String): AccountNoticeResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret")
        val uid = retElement.select("uid").first()?.text()?.toLongOrNull() ?: 0
        val noticeElements = retElement.select("notices")
        val notices = noticeElements.map { nt ->
            NoticeResponse(
                type = nt.select("type").text().toLongOrNull() ?: 0,
                isEmail = (nt.select("isemail").text().toLongOrNull() ?: 0) == 1L,
                isSms = (nt.select("issms").text().toLongOrNull() ?: 0) == 1L,
                isMessenger = (nt.select("ismessenger").text().toLongOrNull() ?: 0) == 1L,
                name = nt.select("name").text(),
                descr = nt.select("descr").text(),
                value = nt.select("value").text(),
            )
        }
        return AccountNoticeResponse(
            uid = uid,
            notices = notices
        )
    }


    fun parseSharedPosts(xmlString: String): List<SharedPostResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        return retElements.map { retElement ->
            SharedPostResponse(
                pid = retElement.select("pid").text().toLongOrNull() ?: 0,
                postBy = retElement.select("postby").text().toLongOrNull() ?: 0,
                postTo = retElement.select("postto").text().toLongOrNull() ?: 0,
                groupId = retElement.select("groupid").text().toLongOrNull() ?: 0,
                uid = retElement.select("uid").text().toLongOrNull() ?: 0,
                postTime = retElement.select("posttime").text(),
                text = retElement.select("text").text(),
                subject = retElement.select("subject").text(),
                sentTime = retElement.select("senttime").text(),
                dtFrom = retElement.select("dtfrom").text(),
                dtTo = retElement.select("dtto").text(),
                emails = retElement.select("emails").text(),
                externUrl = retElement.select("externurl").text(),
                postCatId = retElement.select("postcatid").text().toLongOrNull() ?: 0,
                status = retElement.select("status").text().toLongOrNull() ?: 0,
            )
        }
    }


    fun parseOrderResponse(xmlString: String): List<OrderResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        return retElements.map { retElement ->
            OrderResponse(
                orderId = retElement.select("orderid").text().toLongOrNull() ?: 0,
                agrmId = retElement.select("agrmid").text().toLongOrNull() ?: 0,
                operId = retElement.select("operid").text().toLongOrNull() ?: 0,
                docId = retElement.select("docid").text().toLongOrNull() ?: 0,
                orderNum = retElement.select("ordernum").text(),
                currSumm = retElement.select("currsumm").text().toDoubleOrNull() ?: 0.0,
                taxSumm = retElement.select("taxsumm").text().toDoubleOrNull() ?: 0.0,
                uid = retElement.select("uid").text().toLongOrNull() ?: 0,
                docPayable = retElement.select("docpayable").text().toLongOrNull() ?: 0,
                agrmArchive = retElement.select("agrmarchive").text().toLongOrNull() ?: 0,
                period = retElement.select("period").text(),
                orderDate = retElement.select("orderdate").text(),
                creationDate = retElement.select("creationdate").text(),
                payDate = retElement.select("paydate").text(),
                filename = retElement.select("filename").text(),
                docName = retElement.select("docname").text(),
                docSavePath = retElement.select("docsavepath").text(),
                docTemplate = retElement.select("doctemplate").text(),
                docuPloadext = retElement.select("docuploadext").text(),
                operName = retElement.select("opername").text(),
                agrmNum = retElement.select("agrmnum").text(),
                username = retElement.select("username").text(),
                payReceipt = retElement.select("payreceipt").text(),
                currSymbol = retElement.select("currsymbol").text(),
                billDelivery = retElement.select("billdelivery").text().toLongOrNull() ?: 0,
                billDeliveryName = retElement.select("billdeliveryname").text(),
                exportState = retElement.select("exportstate").text().toShortOrNull() ?: 0,
                exportDate = retElement.select("exportdate").text(),
                organizationId = retElement.select("organizationid").text().toLongOrNull() ?: 0,
                organizationName = retElement.select("organizationname").text(),
            )
        }
    }


    fun parseUsboxServiceFull(xmlString: String): List<UsboxServiceFull> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        return retElements.map { retElement ->
            val serviceElement = retElement.select("service").first()!!
            println(serviceElement.text())
            val currentModifierElement = retElement.select("currentmodifier").first()!!
            println(currentModifierElement.text())
            //   val tarifModifierElement = serviceElement.select("modifiers").first()!!
            UsboxServiceFull(
                service = UsboxServiceResponse(
                    notusbox = serviceElement.select("notusbox").text().toLongOrNull() ?: 0,
                    servid = serviceElement.select("servid").text().toLongOrNull() ?: 0,
                    vgid = serviceElement.select("vgid").text().toLongOrNull() ?: 0,
                    tarid = serviceElement.select("tarid").text().toLongOrNull() ?: 0,
                    catidx = serviceElement.select("catidx").text().toLongOrNull() ?: 0,
                    needcalc = serviceElement.select("needcalc").text().toLongOrNull() ?: 0,
                    noduplicate = serviceElement.select("noduplicate").text().toLongOrNull() ?: 0,
                    planid = serviceElement.select("planid").text().toLongOrNull() ?: 0,
                    installmentduration = serviceElement.select("installmentduration").text().toLongOrNull() ?: 0,
                    state = serviceElement.select("state").text().toLongOrNull() ?: 0,
                    blocked = serviceElement.select("blocked").text().toLongOrNull() ?: 0,
                    installmentfirstpayment = serviceElement.select("installmentfirstpayment").text().toDoubleOrNull()
                        ?: 0.0,
                    mul = serviceElement.select("mul").text().toDoubleOrNull() ?: 0.0,
                    rate = serviceElement.select("rate").text().toDoubleOrNull() ?: 0.0,
                    discount = serviceElement.select("discount").text().toDoubleOrNull() ?: 0.0,
                    blockdiscount = serviceElement.select("blockdiscount").text().toDoubleOrNull() ?: 0.0,
                    personid = serviceElement.select("personid").text(),
                    comment = serviceElement.select("comment").text(),
                    created = serviceElement.select("created").text(),
                    timefrom = serviceElement.select("timefrom").text(),
                    timeto = serviceElement.select("timeto").text(),
                    activated = serviceElement.select("activated").text(),
                    discounttimefrom = serviceElement.select("discounttimefrom").text(),
                    discounttimeto = serviceElement.select("discounttimeto").text(),
                    externaldata = serviceElement.select("externaldata").text(),
                    /* modifiers = TariffModifierResponse(
                         recordId = tarifModifierElement.select("recordid").text().toLongOrNull() ?: 0,
                         tarId = tarifModifierElement.select("tarid").text().toLongOrNull() ?: 0,
                         servId = tarifModifierElement.select("servid").text().toLongOrNull() ?: 0,
                         serviceId = tarifModifierElement.select("serviceid").text().toLongOrNull() ?: 0,
                         vgId = tarifModifierElement.select("vgid").text().toLongOrNull() ?: 0,
                         keepModifier = tarifModifierElement.select("keepmodifier").text().toLongOrNull() ?: 0,
                         userPacketId = tarifModifierElement.select("userpacketid").text().toLongOrNull() ?: 0,
                         actionStaffId = tarifModifierElement.select("actionstaffid").text().toLongOrNull() ?: 0,
                         tarifsRaspId = tarifModifierElement.select("tarifsraspid").text().toLongOrNull() ?: 0,
                         blockState = tarifModifierElement.select("blockstate").text().toLongOrNull() ?: 0,
                         parentRecordId = tarifModifierElement.select("parentrecordid").text().toLongOrNull() ?: 0,
                         personId = tarifModifierElement.select("personid").text().toLongOrNull() ?: 0,
                         rent = tarifModifierElement.select("rent").text().toDoubleOrNull() ?: 0.0,
                         blockRent = tarifModifierElement.select("blockrent").text().toDoubleOrNull() ?: 0.0,
                         rate = tarifModifierElement.select("rate").text().toDoubleOrNull() ?: 0.0,
                         discount = tarifModifierElement.select("discount").text().toDoubleOrNull() ?: 0.0,
                         blockDiscount = tarifModifierElement.select("blockdiscount").text().toDoubleOrNull() ?: 0.0,
                         timeFrom = tarifModifierElement.select("timefrom").text(),
                         timeTo = tarifModifierElement.select("timeto").text(),
                         name = tarifModifierElement.select("name").text(),
                     )*/
                ),
                technicalService = retElement.select("technicalservice").text().toShortOrNull() ?: 0,
                isAssignedIp = retElement.select("isassignedip").text().toLongOrNull() ?: 0,
                isAssignedPhone = retElement.select("isassignedphone").text().toLongOrNull() ?: 0,
                used = retElement.select("used").text().toLongOrNull() ?: 0,
                agrmId = retElement.select("agrmid").text().toLongOrNull() ?: 0,
                uid = retElement.select("uid").text().toLongOrNull() ?: 0,
                common = retElement.select("common").text().toLongOrNull() ?: 0,
                smartCardId = retElement.select("smartcardid").text().toLongOrNull() ?: 0,
                available = retElement.select("available").text().toLongOrNull() ?: 0,
                availableForManager = retElement.select("availableformanager").text().toLongOrNull() ?: 0,
                autoAssign = retElement.select("autoassign").text().toLongOrNull() ?: 0,
                serviceCatId = retElement.select("servicecatid").text().toLongOrNull() ?: 0,
                isUnique = retElement.select("isunique").text().toLongOrNull() ?: 0,
                catAbove = retElement.select("catabove").text().toDoubleOrNull() ?: 0.0,
                includeAbove = retElement.select("includeabove").text().toDoubleOrNull() ?: 0.0,
                totalAmount = retElement.select("totalamount").text().toDoubleOrNull() ?: 0.0,
                agrmNumber = retElement.select("agrmnumber").text(),
                username = retElement.select("username").text(),
                vgLogin = retElement.select("vglogin").text(),
                tarDescr = retElement.select("tardescr").text(),
                catDescr = retElement.select("catdescr").text(),
                personName = retElement.select("personname").text(),
                smartCardSerial = retElement.select("smartcardserial").text(),
                acconDate = retElement.select("accondate").text(),
                symbol = retElement.select("symbol").text(),
                uuid = retElement.select("uuid").text(),
                currentModifier = CurrentModifierResponse(
                    type = currentModifierElement.select("type").text(),
                    value = currentModifierElement.select("value").text().toDoubleOrNull() ?: 0.0,
                ),
                tarType = retElement.select("tartype").text().toShortOrNull() ?: 0,
                isAssignedPromisePayment = retElement.select("isassignedpromisepayment").text().toBoolean(),
            )
        }
    }

    fun parseAvailableServiceResponse(xmlString: String): List<AvailableServiceResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        return retElements.map { retElement ->
            AvailableServiceResponse(
                tarId = retElement.select("tarid").text().toLongOrNull() ?: 0,
                catIdx = retElement.select("catidx").text().toLongOrNull() ?: 0,
                isusBox = retElement.select("isusbox").text().toBoolean(),
                isUnique = retElement.select("isunique").text().toBoolean(),
                descr = retElement.select("descr").text(),
                saleDictServType = retElement.select("saledictservtype").text().toLongOrNull() ?: 0,
                promoPeriod = retElement.select("promoperiod").text().toLongOrNull() ?: 0,
                uuid = retElement.select("uuid").text(),
                serviceType = retElement.select("servicetype").text().toLongOrNull() ?: 0,
                price = retElement.select("price").text().toDoubleOrNull() ?: 0.0,
                includeAbove = retElement.select("includeabove").text().toLongOrNull() ?: 0,
                rentPeriod = retElement.select("rentperiod").text().toLongOrNull() ?: 0,
                rentPeriodMonth = retElement.select("rentperiodmonth").text().toLongOrNull() ?: 0,
            )
        }
    }


    fun parseFullMacStuffResponse(xmlString: String): List<MacStaffResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        return retElements.map {
            parseMacStuffResponse(it)
        }
    }


    fun parseFullSoapClientVgroupFullResponse(xmlString: String): List<ClientVgroupFullResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElements = doc.select("ret")
        return retElements.map { retElement ->
            val vGroupElement = retElement.select("vgroup").first()!!
            val tarRaspElements = retElement.select("tarrasp")
            val staffElements = retElement.select("staff")
            val macStaffElements = retElement.select("macstaff")
            val tarStaffElements = retElement.select("tarstaff")
            val turboShapeElements = retElement.select("turboshape")
            val addonsElements = retElement.select("addons")
            val blockRaspElements = retElement.select("blockrasp")
            val addressesElements = retElement.select("addresses")
            val currentModifier = vGroupElement.select("currentmodifier").first()!!
            ClientVgroupFullResponse(
                vGroup = ClientVgroupResponse(
                    agrmId = vGroupElement.select("agrmid").text().toLongOrNull() ?: 0,
                    vgId = vGroupElement.select("vgid").text().toLongOrNull() ?: 0,
                    blkReq = vGroupElement.select("blkreq").text().toLongOrNull() ?: 0,
                    blocked = vGroupElement.select("blocked").text().toLongOrNull() ?: 0,
                    changed = vGroupElement.select("changed").text().toLongOrNull() ?: 0,
                    agentId = vGroupElement.select("agentid").text().toLongOrNull() ?: 0,
                    tarifId = vGroupElement.select("tarifid").text().toLongOrNull() ?: 0,
                    tarifType = vGroupElement.select("tariftype").text().toLongOrNull() ?: 0,
                    curShape = vGroupElement.select("curshape").text().toLongOrNull() ?: 0,
                    usesmartCards = vGroupElement.select("usesmartcards").text().toLongOrNull() ?: 0,
                    useCas = vGroupElement.select("usecas").text().toLongOrNull() ?: 0,
                    radiusInsertMacStaff = vGroupElement.select("radiusinsertmacstaff").text().toLongOrNull() ?: 0,
                    multiply = vGroupElement.select("multiply").text().toLongOrNull() ?: 0,
                    serviceRent = vGroupElement.select("servicerent").text().toDoubleOrNull() ?: 0.0,
                    serviceVolume = vGroupElement.select("servicevolume").text().toLongOrNull() ?: 0,
                    serviceUsedIn = vGroupElement.select("serviceusedin").text().toLongOrNull() ?: 0,
                    serviceUsedOut = vGroupElement.select("serviceusedout").text().toLongOrNull() ?: 0,
                    agentDescr = vGroupElement.select("agentdescr").text(),
                    tarifDescr = vGroupElement.select("tarifdescr").text(),
                    login = vGroupElement.select("login").text(),
                    currentModifier = CurrentModifierResponse(
                        type = currentModifier.select("type").text(),
                        value = currentModifier.select("value").text().toDoubleOrNull() ?: 0.0,
                    ),
                ),
                tarRasp = tarRaspElements.map { tarRaspElement ->
                    val catDiscountsElements = retElement.select("catdiscounts")
                    TarifsRaspResponse(
                        force = tarRaspElement.select("force").text().toShortOrNull() ?: 0,
                        recordId = tarRaspElement.select("recordid").text().toLongOrNull() ?: 0,
                        vgId = tarRaspElement.select("vgid").text().toLongOrNull() ?: 0,
                        agrmId = tarRaspElement.select("agrmid").text().toLongOrNull() ?: 0,
                        groupId = tarRaspElement.select("groupid").text().toLongOrNull() ?: 0,
                        uid = tarRaspElement.select("uid").text().toLongOrNull() ?: 0,
                        id = tarRaspElement.select("id").text().toLongOrNull() ?: 0,
                        tarIdNew = tarRaspElement.select("taridnew").text().toLongOrNull() ?: 0,
                        tarIdOld = tarRaspElement.select("taridold").text().toLongOrNull() ?: 0,
                        agentType = tarRaspElement.select("agenttype").text().toLongOrNull() ?: 0,
                        tarNewCurId = tarRaspElement.select("tarnewcurid").text().toLongOrNull() ?: 0,
                        tarOldCurId = tarRaspElement.select("taroldcurid").text().toLongOrNull() ?: 0,
                        requestBy = tarRaspElement.select("requestby").text(),
                        changeTime = tarRaspElement.select("changetime").text(),
                        timeTo = tarRaspElement.select("timeto").text(),
                        vgLogin = tarRaspElement.select("vglogin").text(),
                        agrmNum = tarRaspElement.select("agrmnum").text(),
                        code = tarRaspElement.select("code").text(),
                        accName = tarRaspElement.select("accname").text(),
                        agentName = tarRaspElement.select("agentname").text(),
                        tarNewName = tarRaspElement.select("tarnewname").text(),
                        tarOldName = tarRaspElement.select("taroldname").text(),
                        mgrName = tarRaspElement.select("mgrname").text(),
                        tarNewSymbol = tarRaspElement.select("tarnewsymbol").text(),
                        tarOldSymbol = tarRaspElement.select("taroldsymbol").text(),
                        keepAllModifiers = tarRaspElement.select("keepallmodifiers").text().toLongOrNull() ?: 0,
                        keepTarModifier = tarRaspElement.select("keeptarmodifier").text().toLongOrNull() ?: 0,
                        discount = tarRaspElement.select("discount").text().toDoubleOrNull() ?: 0.0,
                        absDiscount = tarRaspElement.select("absdiscount").text().toDoubleOrNull() ?: 0.0,
                        absBlockDiscount = tarRaspElement.select("absblockdiscount").text().toDoubleOrNull() ?: 0.0,
                        rent = tarRaspElement.select("rent").text().toDoubleOrNull() ?: 0.0,
                        blockRent = tarRaspElement.select("blockrent").text().toDoubleOrNull() ?: 0.0,
                        servCatIdx = tarRaspElement.select("servcatidx").text().toLongOrNull() ?: 0,
                        tarNewRentAsService = tarRaspElement.select("tarnewrentasservice").text().toBoolean(),
                        catDiscounts = catDiscountsElements.map { catDiscountsElement ->
                            CategoryDiscountResponse(
                                catIdx = catDiscountsElement.select("catidx").text().toLongOrNull() ?: 0,
                                discount = catDiscountsElement.select("discount").text().toDoubleOrNull() ?: 0.0,
                                above = catDiscountsElement.select("above").text().toDoubleOrNull() ?: 0.0,
                                rate = catDiscountsElement.select("rate").text().toDoubleOrNull() ?: 0.0,
                                includes = catDiscountsElement.select("includes").text().toLongOrNull() ?: 0,
                                parentRecordId = catDiscountsElement.select("parentrecordid").text().toLongOrNull()
                                    ?: 0,
                                keepModifier = catDiscountsElement.select("keepmodifier").text().toLongOrNull() ?: 0,
                            )
                        },
                    )
                },
                staff = staffElements.map { staffElement ->
                    val ipMaskElement = staffElement.select("ipmask").first()!!
                    StaffResponse(
                        recordId = staffElement.select("recordid").text().toLongOrNull() ?: 0,
                        vgId = staffElement.select("vgid").text().toLongOrNull() ?: 0,
                        equipId = staffElement.select("equipid").text().toLongOrNull() ?: 0,
                        type = staffElement.select("type").text().toLongOrNull() ?: 0,
                        asId = staffElement.select("as").text().toLongOrNull() ?: 0,
                        servId = staffElement.select("servid").text().toLongOrNull() ?: 0,
                        servcatIdx = staffElement.select("servcatidx").text().toLongOrNull() ?: 0,
                        createService = staffElement.select("createservice").text().toBoolean(),
                        ipMask = IPMaskResponse(
                            ip = ipMaskElement.select("ip").text(),
                            mask = ipMaskElement.select("mask").text().toIntOrNull() ?: 0,
                            prefix = ipMaskElement.select("prefix").text().toIntOrNull() ?: 0,
                            segmentId = ipMaskElement.select("segmentid").text().toLongOrNull() ?: 0,
                        ),
                    )
                },
                macstaff = macStaffElements.map { macStaffElement ->
                    parseMacStuffResponse(macStaffElement)
                },
                tarstaff = tarStaffElements.map { tarStaffElement ->
                    TariffsStaffResponse(
                        groupId = tarStaffElement.select("groupid").text().toLongOrNull() ?: 0,
                        groupTarId = tarStaffElement.select("grouptarid").text().toLongOrNull() ?: 0,
                        groupModuleId = tarStaffElement.select("groupmoduleid").text().toLongOrNull() ?: 0,
                        tarId = tarStaffElement.select("tarid").text().toLongOrNull() ?: 0,
                        tarType = tarStaffElement.select("tartype").text().toLongOrNull() ?: 0,
                        tarCurId = tarStaffElement.select("tarcurid").text().toLongOrNull() ?: 0,
                        shape = tarStaffElement.select("shape").text().toLongOrNull() ?: 0,
                        rent = tarStaffElement.select("rent").text().toDoubleOrNull() ?: 0.0,
                        tarName = tarStaffElement.select("tarname").text(),
                        tarSymbol = tarStaffElement.select("tarsymbol").text(),
                        tarDescrFull = tarStaffElement.select("tardescrfull").text(),
                    )
                },
                turboshape = turboShapeElements.map { turboShapeElement ->
                    TurboShapeResponse(
                        recordId = turboShapeElement.select("recordid").text().toLongOrNull() ?: 0,
                        vgId = turboShapeElement.select("vgid").text().toLongOrNull() ?: 0,
                        servId = turboShapeElement.select("servid").text().toLongOrNull() ?: 0,
                        tarId = turboShapeElement.select("tarid").text().toLongOrNull() ?: 0,
                        catIdx = turboShapeElement.select("catidx").text().toLongOrNull() ?: 0,
                        shape = turboShapeElement.select("shape").text().toLongOrNull() ?: 0,
                        timeFrom = turboShapeElement.select("timefrom").text(),
                        timeTo = turboShapeElement.select("timeto").text(),
                        descr = turboShapeElement.select("descr").text(),
                        login = turboShapeElement.select("login").text(),
                    )
                },
                addons = addonsElements.map { addonsElement ->
                    VgroupAddonResponse(
                        vgId = addonsElement.select("vgid").text().toLongOrNull() ?: 0,
                        type = addonsElement.select("type").text().toLongOrNull() ?: 0,
                        agentId = addonsElement.select("agentid").text().toLongOrNull() ?: 0,
                        idx = addonsElement.select("idx").text().toLongOrNull() ?: 0,
                        name = addonsElement.select("name").text(),
                        descr = addonsElement.select("descr").text(),
                        strValue = addonsElement.select("strvalue").text(),
                    )
                },
                blockrasp = blockRaspElements.map { blockRaspElement ->
                    BlockRaspResponse(
                        recordId = blockRaspElement.select("recordid").text().toLongOrNull() ?: 0,
                        vgId = blockRaspElement.select("vgid").text().toLongOrNull() ?: 0,
                        groupId = blockRaspElement.select("groupid").text().toLongOrNull() ?: 0,
                        blkReq = blockRaspElement.select("blkreq").text().toLongOrNull() ?: 0,
                        id = blockRaspElement.select("id").text().toLongOrNull() ?: 0,
                        isFreewill = blockRaspElement.select("isfreewill").text().toLongOrNull()==1L,
                        requestBy = blockRaspElement.select("requestby").text().toLongOrNull() ?: 0,
                        unBlockedBy = blockRaspElement.select("unblockedby").text().toLongOrNull() ?: 0,
                        agrmId = blockRaspElement.select("agrmid").text().toLongOrNull() ?: 0,
                        uid = blockRaspElement.select("uid").text().toLongOrNull() ?: 0,
                        changeTime = blockRaspElement.select("changetime").text(),
                        mgrName = blockRaspElement.select("mgrname").text(),
                        mgrDescr = blockRaspElement.select("mgrdescr").text(),
                        comment = blockRaspElement.select("comment").text(),
                        timeTo = blockRaspElement.select("timeto").text(),
                        login = blockRaspElement.select("login").text(),
                        unBlockedManagerLogin = blockRaspElement.select("unblockedmanagerlogin").text(),
                        unBlockedManagerName = blockRaspElement.select("unblockedmanagername").text(),
                        unBlockedManagerDescr = blockRaspElement.select("unblockedmanagerdescr").text(),
                        agrmNum = blockRaspElement.select("agrmnum").text(),
                        username = blockRaspElement.select("username").text(),
                    )
                },
                addresses = addressesElements.map { addressesElement ->
                    AddressBriefResponse(
                        type = addressesElement.select("type").text().toLongOrNull() ?: 0,
                        code = addressesElement.select("code").text(),
                        address = addressesElement.select("address").text(),
                        buildIngUud = addressesElement.select("buildinguuid").text()
                    )
                }
            )
        }
    }

    private fun parseMacStuffResponse(macStaffElement: Element): MacStaffResponse {
        return MacStaffResponse(
            macId = macStaffElement.select("macid").text().toLongOrNull() ?: 0,
            recordId = macStaffElement.select("recordid").text().toLongOrNull() ?: 0,
            vgId = macStaffElement.select("vgid").text().toLongOrNull() ?: 0,
            segment = macStaffElement.select("segment").text(),
            mac = macStaffElement.select("mac").text(),
        )
    }


    fun parseFullSoapTarifFullResponse(xmlString: String): TarifFullResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").first()!!
        val tarifElement = retElement.select("tarif").first()!!
        val settingsElement = retElement.select("settingsrentasservice").first()!!
        return TarifFullResponse(
            tariff = TarifResponse(
                tarId = tarifElement.select("tarid").text().toLongOrNull() ?: 0,
                actualtarId = tarifElement.select("actualtarid").text().toLongOrNull() ?: 0,
                shape = tarifElement.select("shape").text().toLongOrNull() ?: 0,
                traffLimit = tarifElement.select("trafflimit").text().toLongOrNull() ?: 0,
                traffLimitPer = tarifElement.select("trafflimitper").text().toLongOrNull() ?: 0,
                type = tarifElement.select("type").text().toLongOrNull() ?: 0,
                actbLock = tarifElement.select("actblock").text().toLongOrNull() ?: 0,
                archive = tarifElement.select("archive").text().toLongOrNull() ?: 0,
                pricePlan = tarifElement.select("priceplan").text().toLongOrNull() ?: 0,
                traffType = tarifElement.select("trafftype").text().toLongOrNull() ?: 0,
                dailyRent = tarifElement.select("dailyrent").text().toLongOrNull() ?: 0,
                dynamicRent = tarifElement.select("dynamicrent").text().toLongOrNull() ?: 0,
                shapePrior = tarifElement.select("shapeprior").text().toLongOrNull() ?: 0,
                unavaliable = tarifElement.select("unavaliable").text().toLongOrNull() ?: 0,
                rentmultiply = tarifElement.select("rentmultiply").text().toLongOrNull() ?: 0,
                chargeIncoming = tarifElement.select("chargeincoming").text().toLongOrNull() ?: 0,
                curId = tarifElement.select("curid").text().toLongOrNull() ?: 0,
                used = tarifElement.select("used").text().toLongOrNull() ?: 0,
                voipBlockLocal = tarifElement.select("voipblocklocal").text().toLongOrNull() ?: 0,
                dynRoute = tarifElement.select("dynroute").text().toLongOrNull() ?: 0,
                serviceType = tarifElement.select("servicetype").text().toLongOrNull() ?: 0,
                blockRentDuration = tarifElement.select("blockrentduration").text().toLongOrNull() ?: 0,
                rent = tarifElement.select("rent").text().toDoubleOrNull() ?: 0.0,
                blockRent = tarifElement.select("blockrent").text().toDoubleOrNull() ?: 0.0,
                usrBlockRent = tarifElement.select("usrblockrent").text().toDoubleOrNull() ?: 0.0,
                admBlockRent = tarifElement.select("admblockrent").text().toDoubleOrNull() ?: 0.0,
                coefLow = tarifElement.select("coeflow").text().toDoubleOrNull() ?: 0.0,
                coefHigh = tarifElement.select("coefhigh").text().toDoubleOrNull() ?: 0.0,
                catNumbers = tarifElement.select("catnumbers").text().toLongOrNull() ?: 0,
                descr = tarifElement.select("descr").text(),
                descrFull = tarifElement.select("descrfull").text(),
                symbol = tarifElement.select("symbol").text(),
                link = tarifElement.select("link").text(),
                uuid = tarifElement.select("uuid").text(),
                saleDictionAryId = tarifElement.select("saledictionaryid").text().toLongOrNull() ?: 0,
                additional = tarifElement.select("additional").text().toLongOrNull() ?: 0,
                commonIncludes = tarifElement.select("commonincludes").text().toLongOrNull() ?: 0,
                useCommonIncludes = tarifElement.select("usecommonincludes").text().toLongOrNull() ?: 0,
                checkActiveHours = tarifElement.select("checkactivehours").text().toLongOrNull() ?: 0,
                rentAsAervice = tarifElement.select("rentasservice").text().toLongOrNull() ?: 0,
                availableFl = tarifElement.select("availablefl").text().toLongOrNull() ?: 0,
                availableUl = tarifElement.select("availableul").text().toLongOrNull() ?: 0,
                availableSp = tarifElement.select("availablesp").text().toLongOrNull() ?: 0,
                organizationId = tarifElement.select("organizationid").text().toLongOrNull() ?: 0,
                orgtarId = tarifElement.select("orgtarid").text().toLongOrNull() ?: 0,
                organizationName = tarifElement.select("organizationname").text(),
            ),
            settingsRentAsService = TariffsSettingsRentAsServiceResponse(
                includeAbove = settingsElement.select("includeabove").text().toDoubleOrNull() ?: 0.0,
                rentPeriod = settingsElement.select("rentperiod").text().toIntOrNull() ?: 0,
                rentPeriodMonth = settingsElement.select("rentperiodmonth").text().toIntOrNull() ?: 0,
                beginPeriod = settingsElement.select("beginperiod").text().toIntOrNull() ?: 0,
                rent = settingsElement.select("rent").text().toDoubleOrNull() ?: 0.0,
                blockRent = settingsElement.select("blockrent").text().toDoubleOrNull() ?: 0.0,
                usrBlockRent = settingsElement.select("usrblockrent").text().toDoubleOrNull() ?: 0.0,
                admBlockRent = settingsElement.select("admblockrent").text().toDoubleOrNull() ?: 0.0,
            )
        )
    }

    fun parseFullSoapTarifsHistoryResponse(xmlString: String): List<TarifsHistoryResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret")
        return retElement.map { element ->
            TarifsHistoryResponse(
                tarType = element.select("tartype").text().toIntOrNull() ?: 0,
                recordId = element.select("recordid").text().toLongOrNull() ?: 0L,
                vgId = element.select("vgid").text().toLongOrNull() ?: 0L,
                agrmId = element.select("agrmid").text().toLongOrNull() ?: 0L,
                uid = element.select("uid").text().toLongOrNull() ?: 0L,
                id = element.select("id").text().toLongOrNull() ?: 0L,
                tarIdNew = element.select("taridnew").text().toLongOrNull() ?: 0L,
                tarIdOld = element.select("taridold").text().toLongOrNull() ?: 0L,
                requestBy = element.select("requestby").text(),
                raspTime = element.select("rasptime").text(),
                changed = element.select("changed").text(),
                vgLogin = element.select("vglogin").text(),
                agrmNum = element.select("agrmnum").text(),
                code = element.select("code").text(),
                accName = element.select("accname").text(),
                agentName = element.select("agentname").text(),
                tarNewName = element.select("tarnewname").text(),
                tarOldName = element.select("taroldname").text(),
                mgrName = element.select("mgrname").text(),
                mgrDescr = element.select("mgrdescr").text(),
                currSymbol = element.select("currsymbol").text(),
            )
        }
    }


    fun parseFullSoapTarifsStaffResponse(xmlString: String): List<TarifsStaffResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret")
        return retElement.map { element ->
            TarifsStaffResponse(
                groupId = element.select("groupid").text().toLongOrNull() ?: 0L,
                groupTarId = element.select("grouptarid").text().toLongOrNull() ?: 0L,
                groupModuleId = element.select("groupmoduleid").text().toLongOrNull() ?: 0L,
                tarId = element.select("tarid").text().toLongOrNull() ?: 0L,
                tarType = element.select("tartype").text().toLongOrNull() ?: 0L,
                tarCurId = element.select("tarcurid").text().toLongOrNull() ?: 0L,
                shape = element.select("shape").text().toLongOrNull() ?: 0L,
                rent = element.select("rent").text().toDoubleOrNull() ?: 0.0,
                tarName = element.select("tarname").text(),
                tarSymbol = element.select("tarsymbol").text(),
                tarDescrFull = element.select("tardescrfull").text(),
            )
        }
    }

    fun parseFullSoapPaymentsResponse(xmlString: String): ClientPaymentsFullResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret")
        return ClientPaymentsFullResponse(parseRetPayments(retElement))
    }

    private fun parseRetPayments(elements: List<Element>): List<ClientPaymentResponse> {
        return elements.map { element ->
            ClientPaymentResponse(
                pay = parseSoapPayment(element.select("pay").first()!!),
                bsoDoc = element.select("bsodoc").text().toLongOrNull() ?: 0L,
                timestamp = element.select("timestamp").text().toLongOrNull() ?: 0L,
                localtimestamp = element.select("localtimestamp").text().toLongOrNull() ?: 0L,
                amountCurr = element.select("amountcurr").text().toDoubleOrNull() ?: 0.0,
                orderNum = element.select("ordernum").text(),
                uid = element.select("uid").text().toLongOrNull() ?: 0L,
                operId = element.select("operid").text().toLongOrNull() ?: 0L,
                cardNumber = element.select("cardnumber").text().toLongOrNull() ?: 0L,
                currSymb = element.select("currsymb").text(),
                uname = element.select("uname").text(),
                agrm = element.select("agrm").text(),
                mgr = element.select("mgr").text(),
                mgrDescr = element.select("mgrdescr").text(),
                mgrLogin = element.select("mgrlogin").text(),
                operName = element.select("opername").text(),
                login = element.select("login").text(),
                isEps = element.select("iseps").text().toBoolean(),
            )
        }
    }

    fun parseSoapPaymentOrderIdName(elements: List<Element>): List<PaymentOrderIdNameResponse> {
        return elements.map { element ->
            PaymentOrderIdNameResponse(
                orderId = element.select("orderid").text().toLongOrNull() ?: 0L,
                orderNum = element.select("ordernum").text(),
            )
        }
    }

    private fun parseSoapPayment(element: Element): PaymentResponse {
        return PaymentResponse(
            recordId = element.select("recordid").text().toLongOrNull() ?: 0L,
            parentRecordId = element.select("parentrecordid").text().toLongOrNull() ?: 0L,
            agrmId = element.select("agrmid").text(),
            modPerson = element.select("modperson").text().toLongOrNull() ?: 0L,
            currId = element.select("currid").text().toLongOrNull() ?: 0L,
            orderId = element.select("orderid").text().toLongOrNull() ?: 0L,
            status = element.select("status").text().toLongOrNull() ?: 0L,
            classId = element.select("classid").text().toLongOrNull() ?: 0L,
            amount = element.select("amount").text().toDoubleOrNull() ?: 0.0,
            fromAgrmId = element.select("fromagrmid").text().toLongOrNull() ?: 0L,
            revNo = element.select("revno").text().toLongOrNull() ?: 0L,
            revisions = element.select("revisions").text().toLongOrNull() ?: 0L,
            cashCode = element.select("cashcode").text().toLongOrNull() ?: 0L,
            className = element.select("classname").text(),
            payDate = element.select("paydate").text(),
            localDate = element.select("localdate").text(),
            cancelDate = element.select("canceldate").text(),
            periodDate = element.select("perioddate").text(),
            receipt = element.select("receipt").text(),
            comment = element.select("comment").text(),
            uuid = element.select("uuid").text(),
            fromAgrmNumber = element.select("fromagrmnumber").text(),
            paymentOrderNumber = element.select("paymentordernumber").text(),
        )
    }

    private fun parseSoapUprsPayment(element: Element): UprsPaymentResponse {
        return UprsPaymentResponse(
            uprsCurrency = element.select("uprscurrency").text(),
            paymentDate = element.select("paymentdate").text(),
            agentAcceptedDate = element.select("agentaccepteddate").text(),
            acceptedDate = element.select("accepteddate").text(),
            agentOperationId = element.select("agentoperationid").text().toLongOrNull() ?: 0L,
            paymentToolType = element.select("paymenttooltype").text().toLongOrNull() ?: 0L,
            paymentToolNumber = element.select("paymenttoolnumber").text(),
            checkNumber = element.select("checknumber").text().toLongOrNull() ?: 0L,
            terminalNumber = element.select("terminalnumber").text(),
            agentSystemCode = element.select("agentsystemcode").text(),
            agentName = element.select("agentname").text(),
            subscriberId = element.select("subscriberid").text(),
            terminalId = element.select("terminalid").text(),
            terminalAddress = element.select("terminaladdress").text(),
            payerBankAccount = element.select("payerbankaccount").text(),
            payerBankName = element.select("payerbankname").text(),
            payerBik = element.select("payerbik").text(),
            payerBankAddress = element.select("payerbankaddress").text(),
            more = element.select("more").text(),
        )
    }

    fun parseFullSoapResponse(xmlString: String): GetClientAccountResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").firstOrNull()
        return GetClientAccountResponse(parseRet(retElement))
    }


    private fun parseRet(retElement: Element?): ClientAccountDataDto {
        return ClientAccountDataDto(
            application = retElement?.select("application")?.text()?.toIntOrNull() ?: 0,
            billdeliveryname = retElement?.select("billdeliveryname")?.text()?:"",
            account = parseAccount(retElement?.select("account")?.firstOrNull()),
            usergroups = parseUserGroups(retElement?.select("usergroups")?.first()!!),
            agreements = parseAgreements(retElement.select("agreements")!!)
        )
    }

    private fun parseAccount(accountElement: Element?): AccountDto {
        return AccountDto(
            uid = accountElement?.select("uid")?.text()?.toIntOrNull() ?: 0,
            doctype = accountElement?.select("doctype")?.text()?.toIntOrNull() ?: 0,
            ipaccess = accountElement?.select("ipaccess")?.text()?.toIntOrNull() ?: 0,
            billdelivery = accountElement?.select("billdelivery")?.text()?.toIntOrNull() ?: 0,
            category = accountElement?.select("category")?.text()?.toIntOrNull() ?: 0,
            type = accountElement?.select("type")?.text()?.toIntOrNull() ?: 0,
            oksm = accountElement?.select("oksm")?.text()?.toIntOrNull() ?: 0,
            templ = accountElement?.select("templ")?.text()?.toIntOrNull() ?: 0,
            wrongactive = accountElement?.select("wrongactive")?.text()?.toIntOrNull() ?: 0,
            archive = accountElement?.select("archive")?.text()?.toIntOrNull() ?: 0,
            ownership = accountElement?.select("ownership")?.text()?.toIntOrNull() ?: 0,
            mobileisconfirmed = accountElement?.select("mobileisconfirmed")?.text()
                ?.toBooleanStrictOrNull() ?: false,
            emailisconfirmed = accountElement?.select("emailisconfirmed")?.text()
                ?.toBooleanStrictOrNull() ?: false,
            offerisaccepted = accountElement?.select("offerisaccepted")?.text()
                ?.toBooleanStrictOrNull() ?: false,
            soleproprietor = accountElement?.select("soleproprietor")?.text()?.toBooleanStrictOrNull()
                ?: false,
            login = accountElement?.select("login")?.text()?:"",
            pass = accountElement?.select("pass")?.text()?:"",
            descr = accountElement?.select("descr")?.text()?:"",
            name = accountElement?.select("name")?.text()?:"",
            phone = accountElement?.select("phone")?.text()?:"",
            fax = accountElement?.select("fax")?.text()?:"",
            email = accountElement?.select("email")?.text()?:"",
            mobile = accountElement?.select("mobile")?.text()?:"",
            bankname = accountElement?.select("bankname")?.text()?:"",
            branchbankname = accountElement?.select("branchbankname")?.text()?:"",
            treasuryname = accountElement?.select("treasuryname")?.text()?:"",
            treasuryaccount = accountElement?.select("treasuryaccount")?.text()?:"",
            bik = accountElement?.select("bik")?.text()?:"",
            settl = accountElement?.select("settl")?.text()?:"",
            corr = accountElement?.select("corr")?.text()?:"",
            kpp = accountElement?.select("kpp")?.text()?:"",
            inn = accountElement?.select("inn")?.text()?:"",
            ogrn = accountElement?.select("ogrn")?.text()?:"",
            okpo = accountElement?.select("okpo")?.text()?:"",
            okved = accountElement?.select("okved")?.text()?:"",
            gendiru = accountElement?.select("gendiru")?.text()?:"",
            glbuhgu = accountElement?.select("glbuhgu")?.text()?:"",
            kontperson = accountElement?.select("kontperson")?.text()?:"",
            actonwhat = accountElement?.select("actonwhat")?.text()?:"",
            passsernum = accountElement?.select("passsernum")?.text()?:"",
            passno = accountElement?.select("passno")?.text()?:"",
            passissuedate = accountElement?.select("passissuedate")?.text()?:"",
            passissuedep = accountElement?.select("passissuedep")?.text()?:"",
            passissueplace = accountElement?.select("passissueplace")?.text()?:"",
            birthdate = accountElement?.select("birthdate")?.text()?:"",
            birthplace = accountElement?.select("birthplace")?.text()?:"",
            lastmoddate = accountElement?.select("lastmoddate")?.text()?:"",
            wrongdate = accountElement?.select("wrongdate")?.text()?:"",
            okato = accountElement?.select("okato")?.text()?:"",
            uuid = accountElement?.select("uuid")?.text()?:"",
            abonentname = accountElement?.select("abonentname")?.text()?:"",
            abonentsurname = accountElement?.select("abonentsurname")?.text()?:"",
            abonentpatronymic = accountElement?.select("abonentpatronymic")?.text()?:"",
            managerid = accountElement?.select("managerid")?.text()?.toIntOrNull() ?: -1,
            managername = accountElement?.select("managername")?.text()?:"",
            managerlogin = accountElement?.select("managerlogin")?.text()?:"",
            swift = accountElement?.select("swift")?.text()?:"",
            kio = accountElement?.select("kio")?.text()?:"",
            bicbei = accountElement?.select("bicbei")?.text()?:"",
            iban = accountElement?.select("iban")?.text()?:"",
            bankcorr = accountElement?.select("bankcorr")?.text()?:"",
            bankcorrcode = accountElement?.select("bankcorrcode")?.text()?:"",
            bankcorraccount = accountElement?.select("bankcorraccount")?.text()?:"",
            currency = accountElement?.select("currency")?.text()?:"",
            resident = accountElement?.select("resident")?.text()?.toIntOrNull() ?: 0,
            vatonfiscalization = accountElement?.select("vatonfiscalization")?.text()?.toIntOrNull()
                ?: 0,
        )
    }

    private fun parseUserGroups(usergroupsElement: Element): UserGroupsDto {
        return UserGroupsDto(
            usergroups = parseUserGroup(usergroupsElement.select("usergroup")),
            usercnt = usergroupsElement.select("usercnt").text().toIntOrNull() ?: 0,
            fread = usergroupsElement.select("fread").text().toIntOrNull() ?: 0,
            fwrite = usergroupsElement.select("fwrite").text().toIntOrNull() ?: 0,
            defaultgroup = usergroupsElement.select("defaultgroup").text().toIntOrNull() ?: 0,
        )
    }

    private fun parseUserGroup(userGroupsElement: List<Element>): List<UserGroupDto> {
        return userGroupsElement.map { userGroupsElement ->
            UserGroupDto(
                unloadtosorm = userGroupsElement.select("unloadtosorm").text().toIntOrNull() ?: -1,
                sormid = userGroupsElement.select("sormid").text().toIntOrNull() ?: 0,
                groupid = userGroupsElement.select("groupid").text().toIntOrNull() ?: 0,
                promiseallow = userGroupsElement.select("promiseallow").text().toIntOrNull() ?: 0,
                promiseallowmanager = userGroupsElement.select("promiseallowmanager").text()
                    .toIntOrNull() ?: 0,
                promiserent = userGroupsElement.select("promiserent").text().toIntOrNull() ?: 0,
                promisetill = userGroupsElement.select("promisetill").text().toIntOrNull() ?: 0,
                promiseondays = userGroupsElement.select("promiseondays").text().toIntOrNull() ?: 0,
                promiseblockdays = userGroupsElement.select("promiseblockdays").text().toIntOrNull()
                    ?: 0,
                promisemax = userGroupsElement.select("promisemax").text().toIntOrNull() ?: 0,
                promisemin = userGroupsElement.select("promisemin").text().toIntOrNull() ?: 0,
                promiselimit = userGroupsElement.select("promiselimit").text().toIntOrNull() ?: 0,
                name = userGroupsElement.select("name").text(),
                description = userGroupsElement.select("description").text(),
                vendorsormname = userGroupsElement.select("vendorsormname").text(),
                uuid = userGroupsElement.select("uuid").text(),
                uniquegroup = userGroupsElement.select("uniquegroup").text().toIntOrNull() ?: -1,
            )
        }
    }

    private fun parseAgreements(agreementsElements: Elements): List<AgreementDto> {
        val agreements = mutableListOf<AgreementDto>()

        for (agreementElement in agreementsElements) {
            agreements.add(
                AgreementDto(
                    agrmid = agreementElement.select("agrmid").text().toIntOrNull() ?: 0,
                    uid = agreementElement.select("uid").text().toIntOrNull() ?: 0,
                    operid = agreementElement.select("operid").text().toIntOrNull() ?: 0,
                    curid = agreementElement.select("curid").text().toIntOrNull() ?: 0,
                    bnotify = agreementElement.select("bnotify").text().toIntOrNull() ?: 0,
                    archive = agreementElement.select("archive").text().toIntOrNull() ?: 0,
                    vgroups = agreementElement.select("vgroups").text().toIntOrNull() ?: 0,
                    penaltymethod = agreementElement.select("penaltymethod").text().toIntOrNull()
                        ?: 0,
                    monthblockday = agreementElement.select("monthblockday").text().toIntOrNull()
                        ?: 0,
                    agrmtype = agreementElement.select("agrmtype").text().toIntOrNull() ?: 0,
                    balance = agreementElement.select("balance").text().toDoubleOrNull() ?: 0.0,
                    balanceacc = agreementElement.select("balanceacc").text().toDoubleOrNull()
                        ?: 0.0,
                    credit = agreementElement.select("credit").text().toDoubleOrNull() ?: 0.0,
                    promisecredit = agreementElement.select("promisecredit").text().toDoubleOrNull()
                        ?: 0.0,
                    installments = agreementElement.select("installments").text().toDoubleOrNull()
                        ?: 0.0,
                    balancestrictlimit = agreementElement.select("balancestrictlimit").text()
                        .toIntOrNull() ?: 0,
                    blimit = agreementElement.select("blimit").text().toDoubleOrNull() ?: 0.0,
                    balancestatus = agreementElement.select("balancestatus").text().toIntOrNull()
                        ?: 0,
                    isauto = agreementElement.select("isauto").text().toIntOrNull() ?: 0,
                    friendagrmid = agreementElement.select("friendagrmid").text().toIntOrNull()
                        ?: 0,
                    parentagrmid = agreementElement.select("parentagrmid").text().toIntOrNull()
                        ?: 0,
                    paymentmethod = agreementElement.select("paymentmethod").text().toIntOrNull()
                        ?: 0,
                    blockdays = agreementElement.select("blockdays").text().toIntOrNull() ?: 0,
                    blockmonths = agreementElement.select("blockmonths").text().toIntOrNull() ?: 0,
                    orderpayday = agreementElement.select("orderpayday").text().toIntOrNull() ?: 0,
                    blockorders = agreementElement.select("blockorders").text().toIntOrNull() ?: 0,
                    blockamount = agreementElement.select("blockamount").text().toDoubleOrNull()
                        ?: 0.0,
                    priority = agreementElement.select("priority").text().toIntOrNull() ?: 0,
                    ownerid = agreementElement.select("ownerid").text().toIntOrNull() ?: 0,
                    isdefault = agreementElement.select("isdefault").text().toIntOrNull() ?: 0,
                    nofinblock = agreementElement.select("nofinblock").text().toIntOrNull() ?: -1,
                    friendnumber = agreementElement.select("friendnumber").text(),
                    parentnumber = agreementElement.select("parentnumber").text(),
                    balancelimitexceeded = agreementElement.select("balancelimitexceeded").text(),
                    number = agreementElement.select("number").text(),
                    code = agreementElement.select("code").text(),
                    date = agreementElement.select("date").text(),
                    closedon = agreementElement.select("closedon").text(),
                    datevalidto = agreementElement.select("datevalidto").text(),
                    bcheck = agreementElement.select("bcheck").text(),
                    symbol = agreementElement.select("symbol").text(),
                    username = agreementElement.select("username").text(),
                    opername = agreementElement.select("opername").text(),
                    agreementidbopos = agreementElement.select("agreementidbopos").text(),
                    descr = agreementElement.select("descr").text(),
                    balancetext = agreementElement.select("balancetext").text(),
                    initialbalance = agreementElement.select("initialbalance").text()
                        .toDoubleOrNull() ?: 0.0,
                    errormessage = agreementElement.select("errormessage").text()
                )
            )
        }

        return agreements
    }


    fun parseToRecoverPassword(xmlString: String): RecoverPasswordResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").first()!!
        return RecoverPasswordResponse(
            recordId = retElement.select("recordid").text().toIntOrNull() ?: 0,
            destination = retElement.select("destination").text(),
        )
    }

    fun parseToPromisePayment(xmlString: String): List<PromisePaymentResponse> {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val elements = doc.select("ret")
        return elements.map { retElement ->
            PromisePaymentResponse(
                recordId = retElement.select("recordid").text().toLongOrNull() ?: 0L,
                agrmId = retElement.select("agrmid").text().toLongOrNull() ?: 0L,
                payId = retElement.select("payid").text().toLongOrNull() ?: 0L,
                currId = retElement.select("currid").text().toLongOrNull() ?: 0L,
                debt = retElement.select("debt").text().toDoubleOrNull() ?: 0.0,
                amount = retElement.select("amount").text().toDoubleOrNull() ?: 0.0,
                promiseDate = retElement.select("promdate").text(),
                promiseTill = retElement.select("promtill").text(),
                symbol = retElement.select("symbol").text(),
                username = retElement.select("username").text(),
                agrmNum = retElement.select("agrmnum").text(),
                status = retElement.select("status").text().toIntOrNull() ?: 0,
                modPerson = retElement.select("modperson").text().toLongOrNull() ?: 0L,
                cancelPerson = retElement.select("cancelperson").text().toLongOrNull() ?: 0L,
                modFio = retElement.select("modfio").text(),
                cancelFio = retElement.select("cancelfio").text(),
                fee = retElement.select("fee").text().toDoubleOrNull() ?: 0.0,
            )
        }
    }


    fun parseToPPSettings(xmlString: String): PPSettingsResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret")
        return PPSettingsResponse(
            promiseRent = retElement.select("promiserent").text().toLongOrNull() ?: 0L,
            promiseTill = retElement.select("promisetill").text().toLongOrNull() ?: 0L,
            promiseMin = retElement.select("promisemin").text().toDoubleOrNull() ?: 0.0,
            promiseMax = retElement.select("promisemax").text().toDoubleOrNull() ?: 0.0,
            promiseLimit = retElement.select("promiselimit").text().toDoubleOrNull() ?: 0.0,
            promiseOnDays = retElement.select("promiseondays").text().toLongOrNull() ?: 0L,
            promiseBlockDays = retElement.select("promiseblockdays").text().toLongOrNull() ?: 0L,
            promiseAvailable = retElement.select("promiseavailable").text().toLongOrNull() ?: 0L,
            promiseFee = retElement.select("promisefee").text().toDoubleOrNull() ?: 0.0,
        )
    }


    fun parseSetClientInfoResponse(xmlString: String): Long {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").first()
        return retElement?.text()?.toLongOrNull() ?: -1L
    }

    fun parseToDouble(xmlString: String): Double {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").first()
        return retElement?.text()?.toDoubleOrNull() ?: 0.0
    }

    fun parseToPasswordResponse(xmlString: String): PasswordResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").first()
        return PasswordResponse(retElement?.text()?:"")
    }


    fun parseLoginResponse(xml: String?): Long? {
        val uidRegex = Regex("<uid>(\\d+)</uid>")
        return xml?.let {
            uidRegex.find(it)?.groupValues?.get(1)?.toLongOrNull()
        }
    }

    fun parseFaultString(xml: String?): String? {
        // val regex = Regex("<faultstring>(.*?)</faultstring>", RegexOption.DOT_MATCHES_ALL)
        val detail = Regex("<detail>(.*?)</detail>", RegexOption.DOT_MATCHES_ALL)
        return xml?.let { detail.find(it)?.groupValues?.get(1) }
    }

    fun setBaseAppend(innerContent: String): String {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:urn=\"urn:api3\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                innerContent +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>".trimIndent()
    }
}