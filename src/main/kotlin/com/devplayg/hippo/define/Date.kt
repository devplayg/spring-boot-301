package com.devplayg.hippo.define

import org.joda.time.format.DateTimeFormat

const val YYYYMMDD = "yyyy-MM-dd"
const val YYYYMMDDHHMMSS ="yyyy-MM-dd HH:mm:ss"
const val YYYYMMDDHHMMSSZ ="yyyy-MM-dd HH:mm:ssZ"
const val YYYYMMDDHHMMSSZZ = "yyyy-MM-dd'T'HH:mm:ssZZ"
const val YYYYMMDDTHHMMSSSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
const val HHMM = "HH:mm"


val RFC3339Format: org.joda.time.format.DateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern(YYYYMMDDHHMMSSZ)
val RFC3339PatternWithMilli: org.joda.time.format.DateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern(YYYYMMDDTHHMMSSSSSZ)
val RFC3339Pattern: org.joda.time.format.DateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern(YYYYMMDDHHMMSSZZ)
val DefaultDateTimeFormat = DateTimeFormat.forPattern(YYYYMMDDHHMMSS)!!
val DefaultDateFormat = DateTimeFormat.forPattern(YYYYMMDD)!!
val DefaultTimeFormat = DateTimeFormat.forPattern("HH:mm")!!

