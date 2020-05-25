package com.devplayg.hippo.repository

import com.devplayg.hippo.entity.Audit
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class AuditRepo {
    fun findAll() = transaction {
        Audit.all().map { it.toDto() }
    }
}
