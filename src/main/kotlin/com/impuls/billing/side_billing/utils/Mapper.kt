package com.impuls.billing.side_billing.utils

import com.impuls.billing.side_billing.model.in_dto.AvailableServiceResponse
import com.impuls.billing.side_billing.model.in_dto.PPSettingsResponse
import com.impuls.billing.side_billing.model.in_dto.PromisePaymentResponse
import com.impuls.billing.side_billing.model.in_dto.RecoverPasswordResponse
import com.impuls.billing.side_billing.model.in_dto.TarifsHistoryResponse
import com.impuls.billing.side_billing.model.in_dto.TarifsStaffResponse
import com.impuls.billing.side_billing.model.in_dto.client_account.GetClientAccountResponse
import com.impuls.billing.side_billing.model.in_dto.client_payment.ClientPaymentsFullResponse
import com.impuls.billing.side_billing.model.in_dto.client_vgroup.MacStaffResponse
import com.impuls.billing.side_billing.model.in_dto.tariff.TarifFullResponse
import com.impuls.billing.side_billing.model.out_dto.dto.PPSettingsDto
import com.impuls.billing.side_billing.model.out_dto.dto.Account
import com.impuls.billing.side_billing.model.out_dto.dto.Agreement
import com.impuls.billing.side_billing.model.out_dto.dto.AvailableServiceDto
import com.impuls.billing.side_billing.model.out_dto.dto.ClientAccount
import com.impuls.billing.side_billing.model.out_dto.dto.ClientPayment
import com.impuls.billing.side_billing.model.out_dto.dto.MacStaffDto
import com.impuls.billing.side_billing.model.out_dto.dto.Payment
import com.impuls.billing.side_billing.model.out_dto.dto.PromisePaymentDto
import com.impuls.billing.side_billing.model.out_dto.dto.RecoverPassword
import com.impuls.billing.side_billing.model.out_dto.dto.Tarif
import com.impuls.billing.side_billing.model.out_dto.dto.TarifFullDto
import com.impuls.billing.side_billing.model.out_dto.dto.TariffsSettingsRentAsService
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsHistoryDto
import com.impuls.billing.side_billing.model.out_dto.dto.TarifsStaffDto


fun GetClientAccountResponse.toClientAccount(): ClientAccount {
    return ClientAccount(
        billDeliveryName = ret.billdeliveryname,
        account = Account(
            uid = ret.account.uid,
            doctype = ret.account.doctype,
            billDelivery = ret.account.billdelivery,
            category = ret.account.category,
            type = ret.account.type,
            oksm = ret.account.oksm,
            wrongActive = ret.account.wrongactive,
            archive = ret.account.archive,
            ownerShip = ret.account.ownership,
            mobileIsConfirmed = ret.account.mobileisconfirmed,
            emailIsConfirmed = ret.account.emailisconfirmed,
            offerIsAccepted = ret.account.offerisaccepted,
            soleProprietor = ret.account.soleproprietor,
            login = ret.account.login,
            password = ret.account.pass,
            description = ret.account.descr,
            name = ret.account.name,
            phone = ret.account.phone,
            fax = ret.account.fax,
            email = ret.account.email,
            mobile = ret.account.mobile,
            bankName = ret.account.bankname,
            branchBankName = ret.account.branchbankname,
            treasuryName = ret.account.treasuryname,
            treasuryAccount = ret.account.treasuryaccount,
            bik = ret.account.bik,
            setti = ret.account.settl,
            corr = ret.account.corr,
            kpp = ret.account.kpp,
            inn = ret.account.inn,
            ogrn = ret.account.ogrn,
            okpo = ret.account.okpo,
            okved = ret.account.okved,
            abonentName = ret.account.abonentname,
            abonentSurname = ret.account.abonentsurname,
            abonentPatronymic = ret.account.abonentpatronymic
        ),
        agreements = ret.agreements.map {
            Agreement(
                agrmId = it.agrmid,
                uid = it.uid,
                operId = it.operid,
                curId = it.curid,
                bNotify = it.bnotify,
                vGroups = it.vgroups,
                penaltyMethod = it.penaltymethod,
                monthBlockDay = it.monthblockday,
                balance = it.balance,
                credit = it.credit,
                promiseCredit = it.promisecredit,
                installments = it.installments,
                balanceStatus = it.balancestatus,
                friendAgrmId = it.friendagrmid,
                paymentMethod = it.paymentmethod,
                blockDays = it.blockdays,
                blockMonths = it.blockmonths,
                orderPayDay = it.orderpayday,
                blockOrders = it.blockorders,
                blockAmount = it.blockamount,
                ownerId = it.ownerid,
                isDefault = it.isdefault,
                number = it.number,
                date = it.date,
                closeDon = it.closedon,
                bCheck = it.bcheck,
                symbol = it.symbol,
                username = it.username,
                initialBalance = it.initialbalance,
                code = it.code
            )
        }
    )
}

fun RecoverPasswordResponse.toRecoverPassword(): RecoverPassword {
    return RecoverPassword(
        recordId = recordId,
        destination = destination,
    )
}


fun PromisePaymentResponse.toPromisePaymentResponse(): PromisePaymentDto {
    return PromisePaymentDto(
        recordId = recordId,
        agrmId = agrmId,
        payId = payId,
        currId = currId,
        debt = debt,
        amount = amount,
        promiseDate = promiseDate,
        promiseTill = promiseTill,
        symbol = symbol,
        username = username,
        agrmNum = agrmNum,
        status = status,
        modPerson = modPerson,
        cancelPerson = cancelPerson,
        modFio = modFio,
        cancelFio = cancelFio,
        fee = fee,
    )
}


fun ClientPaymentsFullResponse.toClientPayments(): List<ClientPayment> {
    return ret.map { rt ->
        ClientPayment(
            pay = Payment(
                recordId = rt.pay.recordId,
                parentRecordId = rt.pay.parentRecordId,
                modPerson = rt.pay.modPerson,
                currId = rt.pay.currId,
                orderId = rt.pay.orderId,
                status = rt.pay.status,
                classId = rt.pay.classId,
                amount = rt.pay.amount,
                fromAgrmId = rt.pay.fromAgrmId,
                revNo = rt.pay.revNo,
                revisions = rt.pay.revisions,
                cashCode = rt.pay.cashCode,
                className = rt.pay.className,
                payDate = rt.pay.payDate,
                localDate = rt.pay.localDate,
                cancelDate = rt.pay.cancelDate,
                periodDate = rt.pay.periodDate,
                receipt = rt.pay.receipt,
                comment = rt.pay.comment,
                uuid = rt.pay.uuid,
                fromAgrmNumber = rt.pay.fromAgrmNumber,
                paymentOrderNumber = rt.pay.paymentOrderNumber,
            ),
            bsoDoc = rt.bsoDoc,
            timestamp = rt.timestamp,
            localtimestamp = rt.localtimestamp,
            amountCurr = rt.amountCurr,
            orderNum = rt.orderNum,
            uid = rt.uid,
            operId = rt.operId,
            cardNumber = rt.cardNumber,
            currSymb = rt.currSymb,
            uname = rt.uname,
            agrm = rt.agrm,
            mgr = rt.mgr,
            mgrDescr = rt.mgrDescr,
            mgrLogin = rt.mgrLogin,
            operName = rt.operName,
            login = rt.login,
            isEps = rt.isEps,
        )
    }
}


fun TarifsStaffResponse.toTarifsStaff(): TarifsStaffDto {
    return TarifsStaffDto(
        groupId = groupId,
        groupTarId = groupTarId,
        groupModuleId = groupModuleId,
        tarId = tarId,
        tarType = tarType,
        tarCurId = tarCurId,
        shape = shape,
        rent = rent,
        tarName = tarName,
        tarSymbol = tarSymbol,
        tarDescrFull = tarDescrFull,
    )
}

fun TarifsHistoryResponse.toTarifsHistory(): TarifsHistoryDto {
    return TarifsHistoryDto(
        tarType = tarType,
        recordId = recordId,
        vgId = vgId,
        agrmId = agrmId,
        uid = uid,
        id = id,
        tarIdNew = tarIdNew,
        tarIdOld = tarIdOld,
        requestBy = requestBy,
        raspTime = raspTime,
        changed = changed,
        vgLogin = vgLogin,
        agrmNum = agrmNum,
        code = code,
        accName = accName,
        agentName = agentName,
        tarNewName = tarNewName,
        tarOldName = tarOldName,
        mgrName = mgrName,
        mgrDescr = mgrDescr,
        currSymbol = currSymbol,
    )
}


fun TarifFullResponse.toTarifFull(): TarifFullDto {
    return TarifFullDto(
        tarif = Tarif(
            tarId = tariff.tarId,
            actualtarId = tariff.actualtarId,
            shape = tariff.shape,
            traffLimit = tariff.traffLimit,
            traffLimitPer = tariff.traffLimitPer,
            type = tariff.type,
            actbLock = tariff.actbLock,
            archive = tariff.archive,
            pricePlan = tariff.pricePlan,
            traffType = tariff.traffType,
            dailyRent = tariff.dailyRent,
            dynamicRent = tariff.dynamicRent,
            shapePrior = tariff.shapePrior,
            unavaliable = tariff.unavaliable,
            rentmultiply = tariff.rentmultiply,
            chargeIncoming = tariff.chargeIncoming,
            curId = tariff.curId,
            used = tariff.used,
            dynRoute = tariff.dynRoute,
            serviceType = tariff.serviceType,
            voipBlockLocal = tariff.voipBlockLocal,
            blockRentDuration = tariff.blockRentDuration,
            rent = tariff.rent,
            blockRent = tariff.blockRent,
            usrBlockRent = tariff.usrBlockRent,
            admBlockRent = tariff.admBlockRent,
            coefLow = tariff.coefLow,
            coefHigh = tariff.coefHigh,
            catNumbers = tariff.catNumbers,
            descr = tariff.descr,
            descrFull = tariff.descrFull,
            symbol = tariff.symbol,
            link = tariff.link,
            uuid = tariff.uuid,
            saleDictionAryId = tariff.saleDictionAryId,
            additional = tariff.additional,
            commonIncludes = tariff.commonIncludes,
            useCommonIncludes = tariff.useCommonIncludes,
            checkActiveHours = tariff.checkActiveHours,
            rentAsAervice = tariff.rentAsAervice,
            availableFl = tariff.availableFl,
            availableUl = tariff.availableUl,
            availableSp = tariff.availableSp,
            organizationId = tariff.organizationId,
            orgtarId = tariff.orgtarId,
            organizationName = tariff.organizationName,
        ),
        tariffsSettingsRentAsService = TariffsSettingsRentAsService(
            includeAbove = settingsRentAsService.includeAbove,
            rentPeriod = settingsRentAsService.rentPeriod,
            rentPeriodMonth = settingsRentAsService.rentPeriodMonth,
            beginPeriod = settingsRentAsService.beginPeriod,
            rent = settingsRentAsService.rent,
            blockRent = settingsRentAsService.blockRent,
            usrBlockRent = settingsRentAsService.usrBlockRent,
            admBlockRent = settingsRentAsService.admBlockRent,
        )
    )
}

fun MacStaffResponse.toMacStaff(): MacStaffDto {
    return MacStaffDto(
        macId = macId,
        recordId = recordId,
        vgId = vgId,
        segment = segment,
        mac = mac
    )
}

fun AvailableServiceResponse.toAvailableService(): AvailableServiceDto {
    return AvailableServiceDto(
        tarId = tarId,
        catIdx = catIdx,
        isusBox = isusBox,
        isUnique = isUnique,
        descr = descr,
        saleDictServType = saleDictServType,
        promoPeriod = promoPeriod,
        uuid = uuid,
        serviceType = serviceType,
        price = price,
        includeAbove = includeAbove,
        rentPeriod = rentPeriod,
        rentPeriodMonth = rentPeriodMonth
    )
}

fun PPSettingsResponse.toPPSettings(): PPSettingsDto {
    return PPSettingsDto(
        promiseRent = promiseRent == 1L,
        promiseTill = promiseTill,
        promiseMin = promiseMin,
        promiseMax = promiseMax,
        promiseLimit = promiseLimit,
        promiseOnDays = promiseOnDays,
        promiseBlockDays = promiseBlockDays,
        promiseAvailable = promiseAvailable == 1L,
        promiseFee = promiseFee
    )
}




