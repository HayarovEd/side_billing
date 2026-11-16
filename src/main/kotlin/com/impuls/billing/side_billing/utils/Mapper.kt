package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.model.in_dto.ClientInfoResponse
import com.impuls.billing.side_billing.model.in_dto.ClientIpStat
import com.impuls.billing.side_billing.model.in_dto.ClientShortStat
import com.impuls.billing.side_billing.model.in_dto.ClientTarifStat
import com.impuls.billing.side_billing.model.in_dto.OrderResponse
import com.impuls.billing.side_billing.model.in_dto.SharedPostResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.*
import com.impuls.billing.side_billing.model.in_dto.notices.AccountNoticeResponse
import com.impuls.billing.side_billing.model.in_dto.notices.NoticeResponse
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceFull
import com.impuls.billing.side_billing.model.in_dto.usbox_service.UsboxServiceResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.CurrentModifierResponse
import com.impuls.billing.side_billing.model.out_dto.dto.*
import com.impuls.billing.side_billing.model.out_dto.dto.Payment
import com.impuls.billing.soap.generated.*

fun SoapAccountFull.toClientAccount(): ClientAccount {
    val acc = this.account
    return ClientAccount(
        billDeliveryName = this.billdeliveryname ?: "",
        account = Account(
            uid = acc?.uid?.toInt() ?: 0,
            doctype = acc?.doctype?.toInt() ?: 0,
            billDelivery = acc?.billdelivery?.toInt() ?: 0,
            category = acc?.category?.toInt() ?: 0,
            type = acc?.type?.toInt() ?: 0,
            oksm = acc?.oksm?.toInt() ?: 0,
            wrongActive = acc?.wrongactive?.toInt() ?: 0,
            archive = acc?.archive?.toInt() ?: 0,
            ownerShip = acc?.ownership?.toInt() ?: 0,
            mobileIsConfirmed = acc?.isMobileisconfirmed ?: false,
            emailIsConfirmed = acc?.isEmailisconfirmed ?: false,
            offerIsAccepted = acc?.isOfferisaccepted ?: false,
            soleProprietor = acc?.isSoleproprietor ?: false,
            login = acc?.login ?: "",
            password = "", // Security: Do not expose password
            description = acc?.descr ?: "",
            name = acc?.name ?: "",
            phone = acc?.phone ?: "",
            fax = acc?.fax ?: "",
            email = acc?.email ?: "",
            mobile = acc?.mobile ?: "",
            bankName = acc?.bankname ?: "",
            branchBankName = acc?.branchbankname ?: "",
            treasuryName = acc?.treasuryname ?: "",
            treasuryAccount = acc?.treasuryaccount ?: "",
            bik = acc?.bik ?: "",
            setti = acc?.settl ?: "",
            corr = acc?.corr ?: "",
            kpp = acc?.kpp ?: "",
            inn = acc?.inn ?: "",
            ogrn = acc?.ogrn ?: "",
            okpo = acc?.okpo ?: "",
            okved = acc?.okved ?: "",
            abonentName = acc?.abonentname ?: "",
            abonentSurname = acc?.abonentsurname ?: "",
            abonentPatronymic = acc?.abonentpatronymic ?: ""
        ),
        agreements = this.agreements?.map { it.toAgreement() } ?: emptyList()
    )
}

fun SoapAgreement.toAgreement(): Agreement {
    return Agreement(
        agrmId = this.agrmid.toInt(),
        uid = this.uid.toInt(),
        operId = this.operid.toInt(),
        curId = this.curid.toInt(),
        bNotify = this.bnotify?.toInt() ?: 0,
        vGroups = this.vgroups?.toInt() ?: 0,
        penaltyMethod = this.penaltymethod?.toInt() ?: 0,
        monthBlockDay = this.monthblockday?.toInt() ?: 0,
        balance = this.balance ?: 0.0,
        credit = this.credit ?: 0.0,
        code = this.code ?: "",
        promiseCredit = this.promisecredit ?: 0.0,
        installments = this.installments ?: 0.0,
        balanceStatus = this.balancestatus?.toInt() ?: 0,
        friendAgrmId = this.friendagrmid?.toInt() ?: 0,
        paymentMethod = this.paymentmethod?.toInt() ?: 0,
        blockDays = this.blockdays?.toInt() ?: 0,
        blockMonths = this.blockmonths?.toInt() ?: 0,
        orderPayDay = this.orderpayday?.toInt() ?: 0,
        blockOrders = this.blockorders?.toInt() ?: 0,
        blockAmount = this.blockamount ?: 0.0,
        ownerId = this.ownerid?.toInt() ?: 0,
        isDefault = if (this.isdefault == 1L) 1 else 0,
        number = this.number ?: "",
        date = this.date ?: "",
        closeDon = this.closedon,
        bCheck = this.bcheck,
        symbol = this.symbol ?: "",
        username = this.username ?: "",
        initialBalance = this.initialbalance ?: 0.0,
    )
}

fun SoapGetClientInfo.toClientInfoResponse(): ClientInfoResponse {
    return ClientInfoResponse(
        mobileIsConfirmed = this.isMobileisconfirmed ?: false,
        emailIsConfirmed = this.isEmailisconfirmed ?: false,
        offerIsAccepted = this.isOfferisaccepted ?: false,
        passIsTemporary = this.isPassistemporary ?: false,
        triggerContactDataReminder = this.isTriggercontactdatareminder ?: false,
        email = this.email ?: "",
        password = "", // Do not leak password
        phone = this.phone ?: "",
        kontPerson = this.kontperson ?: "",
        mobile = this.mobile ?: ""
    )
}

fun SoapAccountNotices.toAccountNoticeResponse(): AccountNoticeResponse {
    return AccountNoticeResponse(
        uid = this.uid ?: 0L,
        notices = this.notices?.map {
            NoticeResponse(
                type = it.type ?: 0L,
                isEmail = it.isemail == 1L,
                isSms = it.issms == 1L,
                isMessenger = it.ismessenger == 1L,
                name = it.name ?: "",
                descr = it.descr ?: "",
                value = it.value ?: ""
            )
        } ?: emptyList()
    )
}

fun SoapClientVgroupFull.toClientVgroupFull(): ClientVgroupFullResponse {
    val vg = this.vgroup
    return ClientVgroupFullResponse(
        vGroup = ClientVgroupResponse(
            agrmId = vg?.agrmid ?: 0L,
            vgId = vg?.vgid ?: 0L,
            blkReq = vg?.blkreq ?: 0L,
            blocked = vg?.blocked ?: 0L,
            changed = vg?.changed ?: 0L,
            agentId = vg?.agentid ?: 0L,
            tarifId = vg?.tarifid ?: 0L,
            tarifType = vg?.tariftype ?: 0L,
            curShape = vg?.curshape ?: 0L,
            usesmartCards = vg?.usesmartcards ?: 0L,
            useCas = vg?.usecas ?: 0L,
            radiusInsertMacStaff = vg?.radiusinsertmacstaff ?: 0L,
            multiply = vg?.multiply ?: 0L,
            serviceRent = vg?.servicerent ?: 0.0,
            serviceVolume = vg?.servicevolume ?: 0L,
            serviceUsedIn = vg?.serviceusedin ?: 0L,
            serviceUsedOut = vg?.serviceusedout ?: 0L,
            agentDescr = vg?.agentdescr ?: "",
            tarifDescr = vg?.tarifdescr ?: "",
            login = vg?.login ?: "",
            currentModifier = vg?.currentmodifier?.let {
                CurrentModifierResponse(it.type ?: "", it.value ?: 0.0)
            } ?: CurrentModifierResponse("", 0.0)
        ),
        tarRasp = this.tarrasp?.map {
            TarifsRaspResponse(
                force = it.force ?: 0,
                recordId = it.recordid,
                vgId = it.vgid,
                agrmId = it.agrmid ?: 0L,
                groupId = it.groupid ?: 0L,
                uid = it.uid ?: 0L,
                id = it.id,
                tarIdNew = it.taridnew,
                tarIdOld = it.taridold,
                agentType = it.agenttype ?: 0L,
                tarNewCurId = it.tarnewcurid ?: 0L,
                tarOldCurId = it.taroldcurid ?: 0L,
                requestBy = it.requestby ?: "",
                changeTime = it.changetime ?: "",
                timeTo = it.timeto ?: "",
                vgLogin = it.vglogin ?: "",
                agrmNum = it.agrmnum ?: "",
                code = it.code ?: "",
                accName = it.accname ?: "",
                agentName = it.agentname ?: "",
                tarNewName = it.tarnewname ?: "",
                tarOldName = it.taroldname ?: "",
                mgrName = it.mgrname ?: "",
                tarNewSymbol = it.tarnewsymbol ?: "",
                tarOldSymbol = it.taroldsymbol ?: "",
                keepAllModifiers = it.keepallmodifiers ?: 0L,
                keepTarModifier = it.keeptarmodifier ?: 0L,
                discount = it.discount ?: 0.0,
                absDiscount = it.absdiscount ?: 0.0,
                absBlockDiscount = it.absblockdiscount ?: 0.0,
                rent = it.rent ?: 0.0,
                blockRent = it.blockrent ?: 0.0,
                servCatIdx = it.servcatidx ?: 0L,
                tarNewRentAsService = it.isTarnewrentasservice ?: false,
                catDiscounts = emptyList()
            )
        } ?: emptyList(),
        staff = this.staff?.map {
            StaffResponse(
                recordId = it.recordid ?: 0L,
                vgId = it.vgid ?: 0L,
                equipId = it.equipid ?: 0L,
                type = it.type,
                asId = it.`as` ?: 0L,
                servId = it.servid ?: 0L,
                servcatIdx = it.servcatidx ?: 0L,
                createService = it.isCreateservice ?: false,
                ipMask = it.ipmask?.let { ip ->
                    IPMaskResponse(
                        ip = ip.ip ?: "",
                        mask = ip.mask?.toInt() ?: 0,
                        prefix = ip.prefix?.toInt() ?: 0,
                        segmentId = ip.segmentid ?: 0L
                    )
                } ?: IPMaskResponse("", 0, 0, 0L)
            )
        } ?: emptyList(),
        macstaff = this.macstaff?.map {
            MacStaffResponse(
                macId = it.macid ?: 0L,
                recordId = it.recordid ?: 0L,
                vgId = it.vgid ?: 0L,
                segment = it.segment?.toString() ?: "",
                mac = it.mac ?: ""
            )
        } ?: emptyList(),
        tarstaff = this.tarstaff?.map {
            TariffsStaffResponse(
                groupId = it.groupid,
                groupTarId = it.grouptarid,
                groupModuleId = it.groupmoduleid,
                tarId = it.tarid,
                tarType = it.tartype ?: 0L,
                tarCurId = it.tarcurid ?: 0L,
                shape = it.shape ?: 0L,
                rent = it.rent ?: 0.0,
                tarName = it.tarname ?: "",
                tarSymbol = it.tarsymbol ?: "",
                tarDescrFull = it.tardescrfull ?: ""
            )
        } ?: emptyList(),
        turboshape = this.turboshape?.map {
            TurboShapeResponse(
                recordId = it.recordid,
                vgId = it.vgid,
                servId = it.servid ?: 0L,
                tarId = it.tarid ?: 0L,
                catIdx = it.catidx ?: 0L,
                shape = it.shape ?: 0L,
                timeFrom = it.timefrom ?: "",
                timeTo = it.timeto ?: "",
                descr = it.descr ?: "",
                login = it.login ?: ""
            )
        } ?: emptyList(),
        addons = this.addons?.map {
            VgroupAddonResponse(
                vgId = it.vgid,
                type = it.type,
                agentId = it.agentid,
                idx = it.idx,
                name = it.name ?: "",
                descr = it.descr ?: "",
                strValue = it.strvalue ?: ""
            )
        } ?: emptyList(),
        blockrasp = this.blockrasp?.map { it.toBlockRaspResponse() } ?: emptyList(),
        addresses = this.addresses?.map {
            AddressBriefResponse(
                type = it.type,
                code = it.code ?: "",
                address = it.address ?: "",
                buildIngUud = ""
            )
        } ?: emptyList()
    )
}

fun SoapBlockRasp.toBlockRaspResponse(): BlockRaspResponse {
    return BlockRaspResponse(
        recordId = this.recordid ?: 0L,
        vgId = this.vgid ?: 0L,
        groupId = this.groupid ?: 0L,
        blkReq = this.blkreq ?: 0L,
        id = this.id ?: 0L,
        isFreewill = this.isfreewill == 1L,
        requestBy = this.requestby ?: 0L,
        unBlockedBy = this.unblockedby ?: 0L,
        agrmId = this.agrmid ?: 0L,
        uid = this.uid ?: 0L,
        changeTime = this.changetime ?: "",
        mgrName = this.mgrname ?: "",
        mgrDescr = this.mgrdescr ?: "",
        comment = this.comment ?: "",
        timeTo = this.timeto ?: "",
        login = this.login ?: "",
        unBlockedManagerLogin = this.unblockedmanagerlogin ?: "",
        unBlockedManagerName = this.unblockedmanagername ?: "",
        unBlockedManagerDescr = this.unblockedmanagerdescr ?: "",
        agrmNum = this.agrmnum ?: "",
        username = this.username ?: ""
    )
}

fun SoapTarifsStaff.toTarifsStaff(): TarifsStaffDto {
    return TarifsStaffDto(
        groupId = this.groupid,
        groupTarId = this.grouptarid,
        groupModuleId = this.groupmoduleid,
        tarId = this.tarid,
        tarType = this.tartype ?: 0L,
        tarCurId = this.tarcurid ?: 0L,
        shape = this.shape ?: 0L,
        rent = this.rent ?: 0.0,
        tarName = this.tarname ?: "",
        tarSymbol = this.tarsymbol ?: "",
        tarDescrFull = this.tardescrfull ?: ""
    )
}

fun SoapTarifsHistory.toTarifsHistory(): TarifsHistoryDto {
    return TarifsHistoryDto(
        tarType = this.tartype?.toInt() ?: 0,
        recordId = this.recordid ?: 0L,
        vgId = this.vgid ?: 0L,
        agrmId = this.agrmid ?: 0L,
        uid = this.uid ?: 0L,
        id = this.id ?: 0L,
        tarIdNew = this.taridnew ?: 0L,
        tarIdOld = this.taridold ?: 0L,
        requestBy = this.requestby?.toString() ?: "",
        raspTime = this.rasptime ?: "",
        changed = this.changed ?: "",
        vgLogin = this.vglogin ?: "",
        agrmNum = this.agrmnum ?: "",
        code = this.code ?: "",
        accName = this.accname ?: "",
        agentName = this.agentname ?: "",
        tarNewName = this.tarnewname ?: "",
        tarOldName = this.taroldname ?: "",
        mgrName = this.mgrname ?: "",
        mgrDescr = this.mgrdescr ?: "",
        currSymbol = this.currsymbol ?: ""
    )
}

fun SoapTarifFull.toTarifFull(): TarifFullDto {
    val t = this.tarif
    val s = this.settingsrentasservice
    return TarifFullDto(
        tarif = Tarif(
            tarId = t?.tarid ?: 0L,
            actualtarId = t?.actualtarid ?: 0L,
            shape = t?.shape ?: 0L,
            traffLimit = t?.trafflimit ?: 0L,
            traffLimitPer = t?.trafflimitper ?: 0L,
            type = t?.type ?: 0L,
            actbLock = t?.actblock ?: 0L,
            archive = t?.archive ?: 0L,
            pricePlan = t?.priceplan ?: 0L,
            traffType = t?.trafftype ?: 0L,
            dailyRent = t?.dailyrent ?: 0L,
            dynamicRent = t?.dynamicrent ?: 0L,
            shapePrior = t?.shapeprior ?: 0L,
            unavaliable = t?.unavaliable ?: 0L,
            rentmultiply = t?.rentmultiply ?: 0L,
            chargeIncoming = t?.chargeincoming ?: 0L,
            curId = t?.curid ?: 0L,
            used = t?.used ?: 0L,
            dynRoute = t?.dynroute ?: 0L,
            serviceType = t?.servicetype ?: 0L,
            voipBlockLocal = t?.voipblocklocal ?: 0L,
            blockRentDuration = t?.blockrentduration?.toLong() ?: 0L,
            rent = t?.rent ?: 0.0,
            blockRent = t?.blockrent ?: 0.0,
            usrBlockRent = t?.usrblockrent ?: 0.0,
            admBlockRent = t?.admblockrent ?: 0.0,
            coefLow = t?.coeflow ?: 0.0,
            coefHigh = t?.coefhigh ?: 0.0,
            catNumbers = 0L,
            descr = t?.descr ?: "",
            descrFull = t?.descrfull ?: "",
            symbol = t?.symbol ?: "",
            link = t?.link ?: "",
            uuid = t?.uuid?.value ?: "",
            saleDictionAryId = t?.saledictionaryid ?: 0L,
            additional = t?.additional ?: 0L,
            commonIncludes = t?.commonincludes ?: 0L,
            useCommonIncludes = t?.usecommonincludes ?: 0L,
            checkActiveHours = t?.checkactivehours ?: 0L,
            rentAsAervice = t?.rentasservice ?: 0L,
            availableFl = t?.availablefl ?: 0L,
            availableUl = t?.availableul ?: 0L,
            availableSp = 0L,
            organizationId = 0L,
            orgtarId = 0L,
            organizationName = ""
        ),
        tariffsSettingsRentAsService = TariffsSettingsRentAsService(
            includeAbove = s?.includeabove ?: 0.0,
            rentPeriod = s?.rentperiod?.toInt() ?: 0,
            rentPeriodMonth = s?.rentperiodmonth?.toInt() ?: 0,
            beginPeriod = s?.beginperiod?.toInt() ?: 0,
            rent = s?.rent ?: 0.0,
            blockRent = s?.blockrent ?: 0.0,
            usrBlockRent = s?.usrblockrent ?: 0.0,
            admBlockRent = s?.admblockrent ?: 0.0
        )
    )
}

fun SoapMacStaff.toMacStaff(): MacStaffDto {
    return MacStaffDto(
        macId = this.macid ?: 0L,
        recordId = this.recordid ?: 0L,
        vgId = this.vgid ?: 0L,
        segment = this.segment?.toString() ?: "",
        mac = this.mac ?: ""
    )
}

fun AvailableServices.toAvailableService(): AvailableServiceDto {
    return AvailableServiceDto(
        tarId = this.tarid ?: 0L,
        catIdx = this.catidx ?: 0L,
        isusBox = this.isIsusbox ?: false,
        isUnique = this.isIsunique ?: false,
        descr = this.descr ?: "",
        saleDictServType = this.saledictservtype?.toLong() ?: 0L,
        promoPeriod = this.promoperiod ?: 0L,
        uuid = this.uuid ?: "",
        serviceType = this.servicetype?.toLong() ?: 0L,
        price = this.price ?: 0.0,
        includeAbove = this.includeabove?.toLong() ?: 0L,
        rentPeriod = this.rentperiod?.toLong() ?: 0L,
        rentPeriodMonth = this.rentperiodmonth?.toLong() ?: 0L
    )
}

fun SoapUsboxServiceFull.toUsboxServiceFull(): UsboxServiceFull {
    val s = this.service
    val serviceResp = if (s != null) {
        UsboxServiceResponse(
            notusbox = s.notusbox ?: 0L,
            servid = s.servid ?: 0L,
            vgid = s.vgid,
            tarid = s.tarid,
            catidx = s.catidx,
            needcalc = s.needcalc ?: 0L,
            noduplicate = s.noduplicate ?: 0L,
            planid = s.planid ?: 0L,
            installmentduration = s.installmentduration ?: 0L,
            state = s.state ?: 0L,
            blocked = s.blocked ?: 0L,
            installmentfirstpayment = s.installmentfirstpayment ?: 0.0,
            mul = s.mul,
            rate = s.rate ?: 0.0,
            discount = s.discount ?: 0.0,
            blockdiscount = s.blockdiscount ?: 0.0,
            personid = s.personid ?: "",
            comment = s.comment ?: "",
            created = s.created ?: "",
            timefrom = s.timefrom ?: "",
            timeto = s.timeto ?: "",
            activated = s.activated ?: "",
            discounttimefrom = s.discounttimefrom ?: "",
            discounttimeto = s.discounttimeto ?: "",
            externaldata = s.externaldata ?: ""
        )
    } else {
        UsboxServiceResponse(
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0.0, 0.0, 0.0, 0.0, 0.0,
            "", "", "", "", "", "", "", "", ""
        )
    }

    return UsboxServiceFull(
        service = serviceResp,
        technicalService = this.technicalservice ?: 0,
        isAssignedIp = this.isassignedip ?: 0L,
        isAssignedPhone = this.isassignedphone ?: 0L,
        used = this.used ?: 0L,
        agrmId = this.agrmid ?: 0L,
        uid = this.uid ?: 0L,
        common = this.common ?: 0L,
        smartCardId = this.smartcardid ?: 0L,
        available = this.available ?: 0L,
        availableForManager = this.availableformanager ?: 0L,
        autoAssign = this.autoassign ?: 0L,
        serviceCatId = this.servicecatid ?: 0L,
        isUnique = this.isunique ?: 0L,
        catAbove = this.catabove ?: 0.0,
        includeAbove = this.includeabove ?: 0.0,
        totalAmount = this.totalamount ?: 0.0,
        agrmNumber = this.agrmnumber ?: "",
        username = this.username ?: "",
        vgLogin = this.vglogin ?: "",
        tarDescr = this.tardescr ?: "",
        catDescr = this.catdescr ?: "",
        personName = this.personname ?: "",
        smartCardSerial = this.smartcardserial ?: "",
        acconDate = this.accondate ?: "",
        symbol = this.symbol ?: "",
        uuid = this.uuid ?: "",
        currentModifier = this.currentmodifier?.let { CurrentModifierResponse(it.type ?: "", it.value ?: 0.0) } ?: CurrentModifierResponse("", 0.0),
        tarType = this.tartype ?: 0,
        isAssignedPromisePayment = this.isIsassignedpromisepayment ?: false
    )
}

fun SoapPPSettings.toPPSettings(): PPSettingsDto {
    return PPSettingsDto(
        promiseRent = this.promiserent == 1L,
        promiseTill = this.promisetill,
        promiseMin = this.promisemin,
        promiseMax = this.promisemax,
        promiseLimit = this.promiselimit,
        promiseOnDays = this.promiseondays,
        promiseBlockDays = this.promiseblockdays,
        promiseAvailable = this.promiseavailable == 1L,
        promiseFee = this.promisefee
    )
}

fun SoapPromisePayment.toPromisePaymentDto(): PromisePaymentDto {
    return PromisePaymentDto(
        recordId = this.recordid ?: 0L,
        agrmId = this.agrmid ?: 0L,
        payId = this.payid ?: 0L,
        currId = this.currid ?: 0L,
        debt = this.debt ?: 0.0,
        amount = this.amount ?: 0.0,
        promiseDate = this.promdate ?: "",
        promiseTill = this.promtill ?: "",
        symbol = this.symbol ?: "",
        username = this.username ?: "",
        agrmNum = this.agrmnum ?: "",
        status = this.status?.toInt() ?: 0,
        modPerson = this.modperson ?: 0L,
        cancelPerson = this.cancelperson ?: 0L,
        modFio = this.modfio ?: "",
        cancelFio = this.cancelfio ?: "",
        fee = this.fee ?: 0.0
    )
}

fun SoapPaymentFull.toClientPayment(): ClientPayment {
    val p = this.pay
    return ClientPayment(
        pay = Payment(
            recordId = p?.recordid ?: 0L,
            parentRecordId = p?.parentrecordid ?: 0L,
            modPerson = p?.modperson ?: 0L,
            currId = p?.currid ?: 0L,
            orderId = p?.orderid ?: 0L,
            status = p?.status ?: 0L,
            classId = p?.classid ?: 0L,
            amount = p?.amount ?: 0.0,
            fromAgrmId = p?.fromagrmid ?: 0L,
            revNo = p?.revno ?: 0L,
            revisions = p?.revisions ?: 0L,
            cashCode = p?.cashcode ?: 0L,
            className = p?.classname ?: "",
            payDate = p?.paydate ?: "",
            localDate = p?.localdate ?: "",
            cancelDate = p?.canceldate ?: "",
            periodDate = p?.perioddate ?: "",
            receipt = p?.receipt ?: "",
            comment = p?.comment ?: "",
            uuid = p?.uuid ?: "",
            fromAgrmNumber = p?.fromagrmnumber ?: "",
            paymentOrderNumber = p?.paymentordernumber ?: ""
        ),
        bsoDoc = this.bsodoc ?: 0L,
        timestamp = this.timestamp ?: 0L,
        localtimestamp = this.localtimestamp ?: 0L,
        amountCurr = this.amountcurr ?: 0.0,
        orderNum = this.ordernum ?: "",
        uid = this.uid ?: 0L,
        operId = this.operid ?: 0L,
        cardNumber = this.cardnumber ?: 0L,
        currSymb = this.currsymb ?: "",
        uname = this.uname ?: "",
        agrm = this.agrm ?: "",
        mgr = this.mgr ?: "",
        mgrDescr = this.mgrdescr ?: "",
        mgrLogin = this.mgrlogin ?: "",
        operName = this.opername ?: "",
        login = this.login ?: "",
        isEps = this.isIseps ?: false
    )
}

fun SoapOrder.toOrderResponse(): OrderResponse {
    return OrderResponse(
        orderId = this.orderid,
        agrmId = this.agrmid,
        operId = this.operid,
        docId = this.docid,
        orderNum = this.ordernum ?: "",
        currSumm = this.currsumm,
        taxSumm = this.taxsumm,
        uid = this.uid ?: 0L,
        docPayable = this.docpayable ?: 0L,
        agrmArchive = this.agrmarchive,
        period = this.period ?: "",
        orderDate = this.orderdate ?: "",
        creationDate = this.creationdate ?: "",
        payDate = this.paydate ?: "",
        filename = this.filename ?: "",
        docName = this.docname ?: "",
        docSavePath = this.docsavepath ?: "",
        docTemplate = this.doctemplate ?: "",
        docuPloadext = this.docuploadext ?: "",
        operName = this.opername ?: "",
        agrmNum = this.agrmnum ?: "",
        username = this.username ?: "",
        payReceipt = this.payreceipt ?: "",
        currSymbol = this.currsymbol ?: "",
        billDelivery = this.billdelivery ?: 0L,
        billDeliveryName = this.billdeliveryname ?: "",
        exportState = this.exportstate ?: 0,
        exportDate = this.exportdate ?: "",
        organizationId = 0L,
        organizationName = ""
    )
}

fun SoapSharedPost.toSharedPostResponse(): SharedPostResponse {
    return SharedPostResponse(
        pid = this.pid,
        postBy = this.postby ?: 0L,
        postTo = this.postto ?: 0L,
        groupId = this.groupid ?: 0L,
        uid = this.uid ?: 0L,
        postTime = this.posttime ?: "",
        text = this.text ?: "",
        subject = this.subject ?: "",
        sentTime = this.senttime ?: "",
        status = this.status ?: 0L,
        postCatId = this.postcatid ?: 0L,
        dtFrom = this.dtfrom ?: "",
        dtTo = this.dtto ?: "",
        emails = this.emails ?: "",
        externUrl = this.externurl ?: ""
    )
}

fun SoapStat.toClientShortStatList(): List<ClientShortStat> {
    val result = mutableListOf<ClientShortStat>()
    this.data?.forEach { vector ->
        val v: List<String> = vector.`val` ?: emptyList()
        if (v.size >= 15) {
            result.add(
                ClientShortStat(
                    agrmId = v[0].toLongOrNull(),
                    vgId = v[1].toLongOrNull(),
                    agentId = v[2].toLongOrNull(),
                    agentType = v[3].toLongOrNull(),
                    contIn = v[4].toLongOrNull(),
                    cntOut = v[5].toLongOrNull(),
                    duration = v[6].toLongOrNull(),
                    amount = v[7].toDoubleOrNull(),
                    dt = v[8],
                    agentDescr = v[9],
                    currSymbol = v[10],
                    volume = v[11].toDoubleOrNull(),
                    volumeIn = v[12].toDoubleOrNull(),
                    volumeOut = v[13].toDoubleOrNull(),
                    total = v[14].toLongOrNull()
                )
            )
        }
    }
    return result
}

fun SoapStat.toClientTarifStatList(): List<ClientTarifStat> {
    val result = mutableListOf<ClientTarifStat>()
    this.data?.forEach { vector ->
        val v: List<String> = vector.`val` ?: emptyList()
        if (v.size >= 13) {
            result.add(
                ClientTarifStat(
                    agrmId = v[0].toLongOrNull(),
                    vgId = v[1].toLongOrNull(),
                    agentId = v[2].toLongOrNull(),
                    agentType = v[3].toLongOrNull(),
                    agentDescr = v[4],
                    zoneDescr = v[5],
                    currSymbol = v[6],
                    tarId = v[7].toLongOrNull(),
                    catIdx = v[8].toLongOrNull(),
                    volume = v[9].toDoubleOrNull(),
                    volumeIn = v[10].toDoubleOrNull(),
                    volumeOut = v[11].toDoubleOrNull(),
                    amount = v[12].toDoubleOrNull()
                )
            )
        }
    }
    return result
}

fun SoapStat.toClientIpStatList(): List<ClientIpStat> {
    val result = mutableListOf<ClientIpStat>()
    this.data?.forEach { vector ->
        val v: List<String> = vector.`val` ?: emptyList()
        if (v.size >= 11) {
            result.add(
                ClientIpStat(
                    agrmId = v[0].toLongOrNull(),
                    vgId = v[1].toLongOrNull(),
                    agentId = v[2].toLongOrNull(),
                    agentType = v[3].toLongOrNull(),
                    agentDescr = v[4],
                    currSymbol = v[5],
                    srcIp = v[6],
                    volume = v[7].toDoubleOrNull(),
                    volumeIn = v[8].toDoubleOrNull(),
                    volumeOut = v[9].toDoubleOrNull(),
                    amount = v[10].toDoubleOrNull()
                )
            )
        }
    }
    return result
}
