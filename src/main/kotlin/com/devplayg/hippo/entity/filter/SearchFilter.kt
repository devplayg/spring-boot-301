package com.devplayg.hippo.entity.filter

import mu.KLogging
import org.joda.time.DateTime
import org.springframework.format.annotation.DateTimeFormat

open class SearchFilter {
    companion object : KLogging()

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    var startDate: DateTime = DateTime.now()

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    var endDate: String = ""

    var pagingMode: Int = 0

    init {
        //logger.info("asdfasdf")
    }

}
