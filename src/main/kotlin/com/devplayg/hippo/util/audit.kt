package com.devplayg.hippo.util

import com.devplayg.hippo.entity.Audit
import org.jetbrains.kotlin.com.google.gson.Gson

fun auditLog(memberId: Long, category: Int, message: Any) {
    var msg: String
    if (message is String) {
         msg = Gson().toJson(AuditMessage(message))
    } else {
        msg = Gson().toJson(message)
    }
    _auditLog(memberId, category, msg, ipToLong(remoteAddr()))
}

private fun _auditLog(memberId: Long, category: Int, message: String, ip: Long) {
    Audit.new {
        this.memberId = memberId
        this.category = category
        this.message = message
        this.ip = ip
    }
}
