package com.devplayg.hippo.repository

import com.devplayg.hippo.entity.Audit
import com.devplayg.hippo.entity.Audits
import com.devplayg.hippo.entity.filter.AuditFilter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class AuditRepo {
    fun findAll() = transaction {
        Audit.all().map { it.toDto() }
    }

    fun find(filter: AuditFilter) = transaction {
//        val query = condition?.let { StarWarsFilms.select(condition) } ?: StarWarsFilms.selectAll()
        val offset: Int = ((filter.page - 1) % filter.size) * filter.size
//        Audit.all().limit(filter.size, offset.toLong()).map { it.toDto() }
//        Audit.find { Audits.created.between(filter.startDate, filter.endDate) }
//                .limit(filter.size, offset.toLong())
//                .map { it.toDto() }

        // -----------------------------------------------------------------------------
//
//        val condition = when {
//            filter.startDate != null ->
//                Op.build { Audits.created.between(filter.startDate, filter.endDate) }
//            filter.categoryList != null ->
//                Op.build { Audits.category inList filter.categoryList!! }
//            else -> null
//        }
//        condition?.let { Audit.find(it) }
//        val query = condition?.let { Audit.find(condition) } ?: Audit.all()
//        query.limit(filter.size, offset.toLong()).map { it.toDto() }


        // -----------------------------------------------------------------------------
        var cond = Audits.selectAll()
                            .andWhere { Audits.created.between(filter.startDate, filter.endDate) }

        if (filter.categoryList != null) {
            cond.andWhere { Audits.category inList filter.categoryList!! }
        }

        if (filter.sort.length > 0) {
            Audits.columns.forEach {
                if (it.name.equals(filter.sort)) {
                    if (filter.order.toLowerCase().equals("asc")) {
                        filter.sortOrder = Pair(it, SortOrder.ASC)
                        return@forEach
                    }
                    filter.sortOrder = Pair(it, SortOrder.DESC)
                    return@forEach
                }
            }
        }

        cond
                .limit(filter.size, offset.toLong())
                .orderBy(filter.sortOrder)

//        cond.find {  }

//        cond.limit(filter.size, offset.toLong()) }.map { resultRow ->  }
    }
}
