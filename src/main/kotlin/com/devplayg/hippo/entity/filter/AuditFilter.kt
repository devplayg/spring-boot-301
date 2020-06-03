package com.devplayg.hippo.entity.filter

import mu.KLogging
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Seconds

//class AuditFilter(startDate: String, endDate: String, pagingMode: Int)
//    : SearchFilter(startDate, endDate, pagingMode) {
//}

class AuditFilter : SearchFilter() {
    companion object : KLogging()

    init {
        logger.debug("AuditFilter::init() ========================")
        logger.debug("- startDate: {}", startDate)
        logger.debug("- endDate: {}", endDate)
        logger.debug("- pagingMode: {}", pagingMode)
        logger.debug("- dateSearchPeriodDays: {}", dateSearchPeriodDays)

        val memberTz: DateTimeZone = DateTimeZone.forID("America/Los_Angeles")
        val d : DateTime = DateTime.now()
        if (startDate== null) {
            startDate = d.withTimeAtStartOfDay()
        }
        if (endDate== null) {
            endDate = d.withTimeAtStartOfDay().plusSeconds(86400-1)
        }
    }
}
