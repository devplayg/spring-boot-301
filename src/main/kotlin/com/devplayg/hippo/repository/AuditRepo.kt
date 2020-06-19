package com.devplayg.hippo.repository

import com.devplayg.hippo.entity.Audits
import com.devplayg.hippo.entity.filter.AuditFilter
import com.devplayg.hippo.util.getSortOrder
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class AuditRepo {
    fun predicate(filter: AuditFilter): Query {
        val cond = Audits.selectAll()
                .andWhere { Audits.created.between(filter.startDate, filter.endDate) }

        if (filter.categoryList != null) {
            cond.andWhere { Audits.category inList filter.categoryList!! }
        }

        if (filter.message.isNotEmpty()) {
            cond.andWhere { Audits.message.like("%"+filter.message+"%") }
        }
        return cond
    }

    fun find(filter: AuditFilter) = predicate(filter)
            .limit(filter.size, filter.offset())
            .orderBy(getSortOrder(Audits, filter))


//    fun find2(filter: AuditFilter): Any {
//        if (filter.pagingMode == PagingMode.FastPaging.value) {
//            return transaction {
//                predicate(filter)
//                        .limit(filter.size, filter.offset())
//                        .orderBy(getSortOrder(Audits, filter))
//            }
//        }
//        var list: List<AuditDto> = this.find(filter).map {
//            mapToAuditDto(it)
//        }
//        var total: Long = this.predicate(filter).count()
////        transaction {
////            list = this.find(filter).map {
////                mapToAuditDto(it)
////            }
////            total = auditRepo.predicate(filter).count()
////        }
//        return PageData(list, total)
//    }


//        val offset: Int = ((filter.page - 1) % filter.size) * filter.size
//        cond
//                .limit(filter.size, offset.toLong())
//                .orderBy(getSortOrder(Audits, filter))


//    fun findAll(filter: AuditFilter) = transaction {
//        var cond = Audits.selectAll()
//                .andWhere { Audits.created.between(filter.startDate, filter.endDate) }
//
//        if (filter.categoryList != null) {
//            cond.andWhere { Audits.category inList filter.categoryList!! }
//        }
//
//        // Sort
//        var col: Column<*> = Audits.id
//        if (filter.sort.isNotEmpty()) {
//            for (it in Audits.columns) {
//                if (filter.sort == it.name) {
//                    col = it
//                    break
//                }
//            }
//        }
//
//        // Order
//        if (filter.order.toLowerCase() == "asc") {
//            filter.sortOrder = Pair(col, SortOrder.ASC)
//        } else {
//            filter.sortOrder = Pair(col, SortOrder.DESC)
//        }
//
//        val offset: Int = ((filter.page - 1) % filter.size) * filter.size
//        cond
//                .limit(filter.size, offset.toLong())
//                .orderBy(filter.sortOrder)
//    }

//    fun find(filter: AuditFilter) = transaction {
//        val query = condition?.let { StarWarsFilms.select(condition) } ?: StarWarsFilms.selectAll()
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
//        var cond = Audits.selectAll()
//                .andWhere { Audits.created.between(filter.startDate, filter.endDate) }
//
//        if (filter.categoryList != null) {
//            cond.andWhere { Audits.category inList filter.categoryList!! }
//        }
//
//        // Sort
//        var col: Column<*> = Audits.id
//        if (filter.sort.isNotEmpty()) {
//            for (it in Audits.columns) {
//                if (filter.sort == it.name) {
//                    col = it
//                    break
//                }
//            }
//        }
//
//        // Order
//        if (filter.order.toLowerCase() == "asc") {
//            filter.sortOrder = Pair(col, SortOrder.ASC)
//        } else {
//            filter.sortOrder = Pair(col, SortOrder.DESC)
//        }
//
//        val offset: Int = ((filter.page - 1) % filter.size) * filter.size
//        cond
//                .limit(filter.size, offset.toLong())
//                .orderBy(filter.sortOrder)

// -----------------------------------------------------------------------------
//    }
}


