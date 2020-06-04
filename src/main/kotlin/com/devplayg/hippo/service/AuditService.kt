package com.devplayg.hippo.service

import com.devplayg.hippo.entity.Audit
import com.devplayg.hippo.entity.Audits
import com.devplayg.hippo.entity.filter.AuditFilter
import com.devplayg.hippo.entity.mapToAuditDto
import com.devplayg.hippo.repository.AuditRepo
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class AuditService(
        private val auditRepo: AuditRepo
) {
    fun findAll() = transaction {
        auditRepo.findAll()
    }

    //    fun find(filter :AuditFilter) = transaction {
//        auditRepo.find(filter)
//        Audit.find {  }.limit(filter.size, offset )
//        Audits.select {
//        }.offset(10, 10)
//    }
    fun find(filter: AuditFilter) = transaction {
//        var list : List<Audit> = auditRepo.find(filter).map {  }
        auditRepo.find(filter).map {
            mapToAuditDto(it)
        }
    }

}

