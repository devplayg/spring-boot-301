package com.devplayg.hippo.entity.filter

import com.devplayg.hippo.entity.Audits
import mu.KLogging
import org.jetbrains.exposed.sql.SortOrder
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Seconds

//class AuditFilter(startDate: String, endDate: String, pagingMode: Int)
//    : SearchFilter(startDate, endDate, pagingMode) {
//}

class AuditFilter : SearchFilter(lastNDays = 7, sortOrder =  Pair(Audits.id, SortOrder.DESC)) {
    companion object : KLogging()

    var categoryList: List<Int>? = null
    var ip: String = ""
    var message: String = ""

    init {
//        logger.debug("AuditFilter::init() ========================")
//        logger.debug("- startDate: {}", startDate)
//        logger.debug("- endDate: {}", endDate)
//        logger.debug("- pagingMode: {}", pagingMode)
//        logger.debug("- categoryList: {}", categoryList)
//        logger.debug("- sort: {}", sort)
//        logger.debug("- order: {}", order)
        this.debug(this.javaClass.name + "::" + "init()")

        // Sort order
//        sortOrder = Pair(Audits.id, SortOrder.DESC)

        val memberTz: DateTimeZone = DateTimeZone.forID("America/Los_Angeles")
//        val d : DateTime = DateTime.now()
//        if (startDate== null) {
//            startDate = d.withTimeAtStartOfDay()
//        }
//        if (endDate== null) {
//            endDate = d.withTimeAtStartOfDay().plusSeconds(86400-1)
//        }
    }

//    override fun tune() {
//        super.tune()
//    }
    fun debug(info: String="") {
        logger.debug("{} ===========================", info)
        logger.debug("- startDate: {}", startDate)
        logger.debug("- endDate: {}", endDate)
        logger.debug("- pagingMode: {}", pagingMode)
        logger.debug("- categoryList: {}", categoryList)
        logger.debug("- sort: {}", sort)
        logger.debug("- order: {}", order)
        logger.debug("- sortOrder: {}", sortOrder)
    }
}
