package com.devplayg.hippo.service

import com.devplayg.hippo.dao.Audit
import com.devplayg.hippo.repository.AuditRepo
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AuditService(
        private val auditRepo: AuditRepo
) {
    fun findAll() = transaction {
        auditRepo.findAll()
    }
}

