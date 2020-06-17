package com.devplayg.hippo.service

import com.devplayg.hippo.entity.filter.AuditFilter
import com.devplayg.hippo.entity.toAuditDto
import com.devplayg.hippo.repository.AuditRepo
import com.devplayg.hippo.util.PageData
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AuditService(
        private val auditRepo: AuditRepo
) {
    fun getAudits(filter: AuditFilter) = transaction {
        auditRepo.find(filter).map {
            toAuditDto(it)
        }
    }

    fun getAuditsWithTotal(filter: AuditFilter) = transaction {
        PageData(getAudits(filter), auditRepo.predicate(filter).count())
    }
}

//    companion object {
//        fun write(ip: Long, category: Int, message: String) = transaction {
//            Audit.new {
//                this.message = message
//                this.ip = ip
//                this.category = category
//            }
//        }
//    }

//    fun find2(filter: AuditFilter) = transaction{
//        val list :List<AuditDto> = auditRepo.find(filter).map {
//            mapToAuditDto(it)
//        }
//        val total: Long = auditRepo.find(filter).count()
//        PageData(list, total)
//    }

//
//    fun findAll(filter: AuditFilter) = transaction {
//        var q: Query = auditRepo.findAll(filter)
//        val list: List<AuditDto> = q.map {
//            mapToAuditDto(it)
//        }
//        val total: Long = q.count()
//        val total2: Long = q.count()
//
//
////        q.map
////        auditRepo.findAll(filter).map {
////            mapToAuditDto(it)
////        }
//    }

//    fun find(filter :AuditFilter) = transaction {
//        auditRepo.find(filter)
//        Audit.find {  }.limit(filter.size, offset )
//        Audits.select {
//        }.offset(10, 10)
//    }
//    fun find(filter: AuditFilter) = transaction {
////        var list : List<Audit> = auditRepo.find(filter).map {  }
//        auditRepo.find(filter).map {
//            mapToAuditDto(it)
//        }
//    }

//    fun find(filter: AuditFilter) = transaction {
////        var list : List<Audit> = auditRepo.find(filter).map {  }
//        auditRepo.find(filter).map {
//            mapToAuditDto(it)
//        }
//    }
//    }


//
//fun getMatchedColumn(cols: List<Column<*>>, colName: String): Column<*>? {
//    for (it in cols) {
//        if (colName == it.name) {
//            return it
//        }
//    }
//    return null
//}
