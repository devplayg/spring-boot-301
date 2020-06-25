package com.devplayg.hippo.filter

import com.devplayg.hippo.entity.Audits
import mu.KLogging
import org.jetbrains.exposed.sql.SortOrder

class AuditFilter : DateRangeFilter(lastNDays = 7, sortOrder =  Pair(Audits.id, SortOrder.DESC)) {
    companion object : KLogging()

    var categoryList: List<Int>? = arrayListOf()
    var ip: String = ""
    var message: String = ""

    init {
        this.debug(this.javaClass.name + "::" + "init()")

        // Sort order
//        sortOrder = Pair(Audits.id, SortOrder.DESC)

        //val memberTz: DateTimeZone = DateTimeZone.forID("America/Los_Angeles")
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
        logger.debug("=========================== {}", info)
        logger.debug("- startDate: {}", startDate)
        logger.debug("- endDate: {}", endDate)
        logger.debug("- pagingMode: {}", pagingMode)
        logger.debug("- size: {}", size)
//        logger.debug("- order: {}", order)
        logger.debug("- sortOrder: {}", sortOrder)
        logger.debug("- categoryList: {}", categoryList)
    }
}
