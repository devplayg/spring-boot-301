package com.devplayg.hippo.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

fun remoteAddr(): String {
    if (RequestContextHolder.getRequestAttributes() == null) {
        return "127.0.0.1"
    }
    val req = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

    var ip = req.getHeader("X-FORWARDED-FOR")
    if (ip == null) {
        ip = req.remoteAddr
    }
    return ip
}

fun ipToLong(ipAddress: String): Long {
    var result: Long = 0
    val ipAddressInArray = ipAddress.split(".").toTypedArray()
    for (i in 3 downTo 0) {
        val ip = ipAddressInArray[3 - i].toLong()
        result = result or (ip shl i * 8)
    }
    return result
}
