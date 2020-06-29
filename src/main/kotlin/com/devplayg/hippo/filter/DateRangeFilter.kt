package com.devplayg.hippo.filter

import com.devplayg.hippo.define.PagingMode
import com.devplayg.hippo.define.RFC3339Format
import mu.KLogging
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder
import org.joda.time.DateTime
import org.springframework.format.annotation.DateTimeFormat
import java.time.ZoneId
import java.util.*

open class DateRangeFilter(
        val lastNDays: Int,
        sortOrder: Pair<Expression<*>, SortOrder>
) : Filter(PagingMode.FastPaging.value, 1, 15, sortOrder) {

    // Logging
    companion object : KLogging()

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssZ")
    lateinit var startDate: DateTime

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssZ")
    lateinit var endDate: DateTime

    var dateRange: String = ""
    var timezone: String = TimeZone.getDefault().toZoneId().id

    fun tune() {
        if (this.dateRange.isEmpty()) {
            this.setDateDefault()
            return
        }

        val dateArr: List<String> = dateRange.split(" - ")
        if (dateArr.size !== 2) {
            this.setDateDefault()
            return
        }
        try {
            val offset = ZoneId.of(timezone).rules.getOffset(java.time.LocalDateTime.now()).id // Get timezone offset
            this.startDate = RFC3339Format.parseDateTime(dateArr[0] + ":00" + offset)
            this.endDate = RFC3339Format.parseDateTime(dateArr[1] + ":59" + offset)
        } catch (e: IllegalArgumentException) {
            logger.error("{}; {}", this.toString(), e.message)
            this.setDateDefault()
        }

    }

    private fun setDateDefault() {
        val now: DateTime = DateTime.now()
        if (!::startDate.isInitialized) {
            startDate = now.withTimeAtStartOfDay().plusDays(this.lastNDays * -1)
        }
        if (!::endDate.isInitialized) {
            endDate = now.withTimeAtStartOfDay().plusSeconds(86400 - 1)
        }
    }
}
