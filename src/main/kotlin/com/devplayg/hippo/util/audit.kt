package com.devplayg.hippo.util

import com.devplayg.hippo.entity.Audit
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.kotlin.com.google.gson.Gson
import javax.lang.model.type.NullType

data class AuditMessage(val message: String)

fun auditLog(memberId: Int, category: Int, message: Any? = null) {
    val msg:String? = when (message) {
        is NullType -> {
            null
        }
        is String -> {
            Gson().toJson(AuditMessage(message))
        }
        else -> {
            Gson().toJson(message)
        }
    }
    _auditLog(memberId, category, msg, ipToLong(remoteAddr()))
}

private fun _auditLog(memberId: Int, category: Int, message: String?, ip: Long) = transaction {
    Audit.new {
        this.memberId = memberId
        this.category = category
        this.message = message
        this.ip = ip
    }
}
