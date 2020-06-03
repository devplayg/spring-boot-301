package com.devplayg.hippo.entity.filter

import mu.KLogging
import org.joda.time.DateTime
import org.springframework.format.annotation.DateTimeFormat

open class SearchFilter {
    companion object : KLogging()

//    constructor(startDate: DateTime, endDate: DateTime, pagingMode: Int) {
//    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    var startDate: DateTime? = null


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    var endDate: DateTime? = null


    var pagingMode: Int = 0

    var dateSearchPeriodDays: Int = 0

    //    constructor() {
    init {

        logger.debug("SearchFilter::init() ========================")
        logger.debug("- startDate: {}", startDate)
        logger.debug("- endDate: {}", endDate)
        logger.debug("- pagingMode: {}", pagingMode)
        logger.debug("- dateSearchPeriodDays: {}", dateSearchPeriodDays)
//        if(!::endDate.isInitialized){
//            endDate = DateTime.now()
//        }
//    constructor() {
//        if (this::startDate.isInitialized) {
//
//        }
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
