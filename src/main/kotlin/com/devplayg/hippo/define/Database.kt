package com.devplayg.hippo.define

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

var DBTZ = DateTimeZone.UTC

fun DateTime.toDBTZ(): String {
    return this.withZone(DBTZ).toString("yyyy-MM-dd HH:mm:ss")
}
