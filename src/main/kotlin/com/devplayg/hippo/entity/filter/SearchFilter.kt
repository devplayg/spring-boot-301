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
    }

    // Logging
    companion object : KLogging()

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    lateinit var startDate: DateTime

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    lateinit var endDate: DateTime


    var pagingMode: Int = 0

    var page: Int = 1
    var size: Int = 15
    var sort: String = ""
    var order: String = ""


    //    var so : Pair<Expression<*>, SortOrder>? = null
    lateinit var sortOrder: Pair<Expression<*>, SortOrder>


//    var dateSearchPeriodDays: Int = 0
//    startDate: 2020-05-28 00:00
//    endDate: 2020-06-04 23:59
//    pageSize: 20
//    pagingMode: 2
//    page: 0
//    size: 400
//    sort: id,asc


    //    constructor() {
    init {
//        if (::startDate.isInitialized) {
//            logger.debug("- startDate: {}", startDate)
//        }

        logger.debug("SearchFilter::init() ========================")
//        logger.debug("- startDate: {}", startDate)
//        logger.debug("- endDate: {}", endDate)
        logger.debug("- pagingMode: {}", pagingMode)
//        if(!::endDate.isInitialized){
//            endDate = DateTime.now()
//        }
//    constructor() {
//    }
//        endDate = DateTime.now()
//        logger.debug("endDate: {}", endDate)
//        logger.info("asdfasdf")
//        if (endD ate == null) {
//
//        }
    }

//    fun tune() {
//    }

}
