package com.devplayg.hippo.service

import com.devplayg.hippo.entity.Audits
import com.devplayg.hippo.repository.AuditRepo
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AuditService(
        private val auditRepo: AuditRepo
) {
    fun findAll() = transaction {
        auditRepo.findAll()
    }
//    fun find() = transaction {
//        Audits.select {
//        }.offset(10, 10)
//    }
}

