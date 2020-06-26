package com.devplayg.hippo.filter

import com.devplayg.hippo.entity.Audits
import mu.KLogging
import org.jetbrains.exposed.sql.SortOrder

class AuditFilter : DateRangeFilter(lastNDays = 7, sortOrder =  Pair(Audits.id, SortOrder.DESC)) {
    companion object : KLogging()

    var categoryList: List<Int>? = arrayListOf()
    var ip: String = ""
    var message: String = ""

    fun debug(info: String="") {
        logger.debug("=========================== {}", info)
        logger.debug("- startDate: {}", startDate)
        logger.debug("- endDate: {}", endDate)
        logger.debug("- dateRange: {} [{}]", dateRange, timezone)
        logger.debug("- pagingMode: {}", pagingMode)
        logger.debug("- size: {}", size)
        logger.debug("- sortOrder: {}", sortOrder)
        logger.debug("- categoryList: {}", categoryList)
    }
}
