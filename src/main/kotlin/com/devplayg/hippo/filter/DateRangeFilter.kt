package com.devplayg.hippo.filter

import com.devplayg.hippo.define.PagingMode
import com.devplayg.hippo.define.RFC3339Format
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.util.currentUsername
import mu.KLogging
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.format.annotation.DateTimeFormat
import java.time.ZoneId
import java.util.*

open class DateRangeFilter(
        val lastNDays: Int,
        sortOrder: Pair<Expression<*>, SortOrder>
) : Filter(PagingMode.FastPaging.value, 1, 15, sortOrder) {

    // Logging
    companion object : KLogging()

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    lateinit var startDate: DateTime

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    lateinit var endDate: DateTime

    // Date range
    var dateRange: String = ""

    // Splilled Date & time
    var sDate: String = ""
    var sTime: String = ""
    var eDate: String = ""
    var eTime: String = ""

    // Timezone
    var timezone: String = ""
    var memberTz: String = ""
    var timezoneOffset: String = ""

    open fun tune(memberCacheRepo: MemberCacheRepo) {
        this.tuneTimezone(memberCacheRepo)

        this.tuneDate()
        if (this.sort.isEmpty()) {
            this.sort = (sortOrder.first as Column).name
        }
    }

    fun tuneTimezone(memberCacheRepo: MemberCacheRepo) {
        // Timezone
        this.memberTz = memberCacheRepo.findByUsername(currentUsername())?.timezone ?: DateTimeZone.getDefault().id

        // Timezone offset
        this.timezoneOffset = if (this.timezone.isBlank()) {
            ZoneId.of(this.memberTz).rules.getOffset(java.time.LocalDateTime.now()).id
        } else {
            ZoneId.of(this.timezone).rules.getOffset(java.time.LocalDateTime.now()).id
        }
    }

    fun tuneDate() {
        // Standard date input
        if (::startDate.isInitialized && ::endDate.isInitialized) {
            this.sDate = DefaultDateFormat.print(startDate)
            this.sTime = DefaultTimeFormat.print(startDate)
            this.eDate = DefaultDateFormat.print(endDate)
            this.eTime = DefaultTimeFormat.print(endDate)
            return
        }

        try {
            if (this.sDate.isNotBlank()) { // Splitted date and time
                this.tuneDateUsingSplittedDates()
                return
            }

            if (this.dateRange.isNotBlank()) { // // Date range
                this.tuneDateUsingDateRange()
                return
            }
        } catch (e: IllegalArgumentException) {
            logger.error("{}; {}", this.toString(), e.message)
            logger.error("filter: {}", this)
            this.setDateDefault()
            return
        }

        this.setDateDefault()
    }

    fun tuneDateUsingSplittedDates() {
        var start = "$sDate "
        start += if (sTime.isBlank()) {
            this.sTime="00:00"
            defaultStartTime()
        } else {
            "$sTime:00"
        }
        this.startDate = RFC3339Format.parseDateTime("$start$timezoneOffset")

        if (eDate.isBlank()) {
            this.endDate = defaultStartDate(DateTime.now())
            return
        }

        var end = "$eDate "
        end += if (eTime.isBlank()) {
            this.eTime="23:59"
            defaultEndTime()
        } else {
            "$eTime:59"
        }
        this.endDate = RFC3339Format.parseDateTime("$end$timezoneOffset")
        return
    }

    fun tuneDateUsingDateRange() {
        val dateArr: List<String> = dateRange.split(" - ")
        if (dateArr.size !== 2) {
            this.setDateDefault()
            return
        }
        this.startDate = RFC3339Format.parseDateTime(dateArr[0] + ":00" + timezoneOffset)
        this.endDate = RFC3339Format.parseDateTime(dateArr[1] + ":59" + timezoneOffset)
    }

    private fun setDateDefault() {
        val now: DateTime = DateTime.now()
        this.startDate = now.withTimeAtStartOfDay().plusDays(this.lastNDays * -1)
        this.endDate = now.withTimeAtStartOfDay().plusSeconds(86400 - 1)

        this.sDate = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd").print(this.startDate)
        this.sTime = "00:00"
        this.eDate = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd").print(this.endDate)
        this.eTime = "23:59"
    }

    fun defaultStartDate(d: DateTime) = d.withTimeAtStartOfDay().plusDays(this.lastNDays * -1) // YYYY-MM-DD 00:00:00
    fun defaultEndDate(d: DateTime) = d.withTimeAtStartOfDay().plusSeconds(86400 - 1) // YYYY-MM-DD 23:59:59
    fun defaultStartTime() = "00:00:00"
    fun defaultEndTime() = "23:59:59"
    // fun defaultOffset() = ZoneId.of(TimeZone.getDefault().toZoneId().id).rules.getOffset(java.time.LocalDateTime.now()).id

}
