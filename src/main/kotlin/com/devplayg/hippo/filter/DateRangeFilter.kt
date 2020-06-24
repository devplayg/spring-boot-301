package com.devplayg.hippo.filter

import com.devplayg.hippo.define.PagingMode
import mu.KLogging
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder
import org.joda.time.DateTime
import org.springframework.format.annotation.DateTimeFormat

open class DateRangeFilter(
        var lastNDays: Int,
        sortOrder: Pair<Expression<*>, SortOrder>
) : Filter(PagingMode.FastPaging.value, 1, 10, sortOrder) {

    // Logging
    companion object : KLogging()

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssZ")
    lateinit var startDate: DateTime

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssZ")
    lateinit var endDate: DateTime

    init {
        val now: DateTime = DateTime.now()
        if (!::startDate.isInitialized) {
            startDate = now.withTimeAtStartOfDay().plusDays(lastNDays * -1)
        }
        if (!::endDate.isInitialized) {
            endDate = now.withTimeAtStartOfDay().plusSeconds(86400 - 1)
        }
    }
}
