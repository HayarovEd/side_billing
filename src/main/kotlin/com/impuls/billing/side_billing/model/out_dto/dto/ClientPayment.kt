package com.impuls.billing.side_billing.model.out_dto.dto


data class ClientPayment(
    val pay: Payment,
    val bsoDoc: Long,
    val timestamp: Long,
    val localtimestamp: Long,
    val amountCurr: Double,
    val orderNum: String,
    val uid: Long,
    val operId: Long,
    val cardNumber: Long,
    val currSymb: String,
    val uname: String,
    val agrm: String,
    val mgr: String,
    val mgrDescr: String,
    val mgrLogin: String,
    val operName: String,
    val login: String,
    val isEps: Boolean,
)
