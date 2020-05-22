package com.devplayg.hippo.service

import com.devplayg.hippo.dao.Audits
import com.devplayg.hippo.dao.rowToAudit
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AuditService {

    fun all() = transaction {
        Audits.selectAll().map { it.toAudit() }
    }

    private fun ResultRow.toAudit() = Audits.rowToAudit(this)
}

