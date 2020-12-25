package com.devplayg.hippo.filter

import com.devplayg.hippo.define.PagingMode
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder

open class Filter(
    // Paging mode
    var pagingMode: Int = PagingMode.FastPaging.value,

    // Default start page
    var page: Int = 1,

    // Default page size
    var pageSize: Int = 20,

    // Default sort and order
    var sortOrder: Pair<Expression<*>, SortOrder>
) {
    var sort: String = ""
    var size: Int = 0
    var order: String = ""

    init {
        this.sort = (sortOrder.first as Column).name
        this.order = sortOrder.second.name
    }

    fun offset() = ((this.page - 1) * this.size).toLong()
}
