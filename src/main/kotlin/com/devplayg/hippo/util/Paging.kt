package com.devplayg.hippo.util

import com.devplayg.hippo.entity.filter.SearchFilter
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder


class PageData(val rows: Any, val total: Long)

fun getSortOrder(table: LongIdTable, filter: SearchFilter): Pair<Expression<*>, SortOrder> {
    val col: Column<*> = getMatchedColumn(table, filter.sort)
    // Order
    if (filter.order.toLowerCase() == "asc") {
        return Pair(col, SortOrder.ASC)
    }
    return Pair(col, SortOrder.DESC)
}

fun getMatchedColumn(table: LongIdTable, colName: String): Column<*> {
    for (it in table.columns) {
        if (colName == it.name) {
            return it
        }
    }
    return table.id
}
