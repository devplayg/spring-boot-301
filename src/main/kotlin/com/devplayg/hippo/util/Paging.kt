package com.devplayg.hippo.util

import com.devplayg.hippo.filter.DateRangeFilter
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder


class PageData {
    lateinit var rows: Any
    var total: Long

    constructor() {
        this.total = 0
    }

    constructor(rows: Any, total: Long) {
        this.rows = rows
        this.total = total
    }
}

fun getSortOrder(table: LongIdTable, filter: DateRangeFilter): Pair<Expression<*>, SortOrder> {
    val col: Column<*> = getMatchedColumn(table, filter.sort)
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
