package com.devplayg.hippo.filter

import com.devplayg.hippo.define.PagingMode
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder

open class Filter(
        var pagingMode: Int = PagingMode.FastPaging.value,
        var page: Int = 1,
        var size: Int = 20,
        var sortOrder: Pair<Expression<*>, SortOrder>
) {
    var sort: String = ""
    var order: String = ""

    init {
        this.order = sortOrder.second.name
    }

    fun offset() = ((this.page - 1) * this.size).toLong()
}
