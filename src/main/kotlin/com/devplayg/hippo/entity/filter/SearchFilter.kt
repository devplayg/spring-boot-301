package com.devplayg.hippo.entity.filter

import mu.KLogging
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder
import org.joda.time.DateTime
import org.springframework.format.annotation.DateTimeFormat

open class SearchFilter {

    // Set the date range to the last 3 days
    // dateRangeToLastNdDays
    constructor(lastNDays: Int) {
        val now: DateTime = DateTime.now()

        if (!::startDate.isInitialized) {
            startDate = now.withTimeAtStartOfDay().plusDays(lastNDays * -1)
        }
        if (!::endDate.isInitialized) {
            endDate = now.withTimeAtStartOfDay().plusSeconds(86400 - 1)
        }
    }

    constructor(lastNDays: Int, sortOrder: Pair<Expression<*>, SortOrder>) : this(lastNDays) {
        this.sortOrder = sortOrder
        this.order = this.sortOrder.second.name
    }

    // Logging
    companion object : KLogging()

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssZ")
    lateinit var startDate: DateTime

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssZ")
    lateinit var endDate: DateTime

    // Paging
    var pagingMode: Int = 2
    var page: Int = 1
    var size: Int = 10
    var sort: String = ""
    var order: String = ""


    //    var so : Pair<Expression<*>, SortOrder>? = null
    lateinit var sortOrder: Pair<Expression<*>, SortOrder>

    //    constructor() {
    init {
//        if (::startDate.isInitialized) {
//            logger.debug("- startDate: {}", startDate)
//        }


        logger.debug("SearchFilter::init() ========================")
//        logger.debug("- startDate: {}", startDate)
//        logger.debug("- endDate: {}", endDate)
        logger.debug("- pagingMode: {}", pagingMode)
        logger.debug("- sort: {}", sort)
        logger.debug("- order: {}", order)
//        if(!::endDate.isInitialized){
//            endDate = DateTime.now()
//        }
    }

//    open fun tune() {
//        if (this.order.isEmpty()) {
//            this.order = this.sortOrder.second.name
//        }
//    }

    fun offset() = ((this.page -1) * this.size).toLong()

}
