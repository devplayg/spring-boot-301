package com.devplayg.hippo.util

import com.devplayg.hippo.define.authoritiesToValue
import com.devplayg.hippo.framework.CustomUserDetails
import org.jetbrains.exposed.sql.SortOrder
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.*


/**
 * 현재 Username 조회
 */
fun currentUsername(): String {
    val auth = SecurityContextHolder.getContext().authentication
    return if (auth is AnonymousAuthenticationToken) {
        ""
    } else {
        auth.name
    }
}


/**
 * 현재 Username & Roles value 조회
 */
fun currentUsernameAndRoles(): Pair<String, Int> {
    val auth = SecurityContextHolder.getContext().authentication
    return if (auth is AnonymousAuthenticationToken) {
        Pair("", 0)
    } else {
        Pair(auth.name!!, authoritiesToValue(auth.authorities))
    }
}


/**
 * 문자열을 SortOrder로 변환
 */
fun toSortOrder(order: String): SortOrder {
    return if (order.trim().toLowerCase() == "asc") {
        SortOrder.ASC
    } else {
        SortOrder.DESC
    }
}

/**
 * Base64 URL Encoding/Decoding
 */
fun base64UrlEncode(str: String) = Base64.getEncoder().encodeToString(str.toByteArray(Charsets.UTF_8))!!
fun base64UrlDecode(str: String) = String(Base64.getUrlDecoder().decode(str))


/**
 * Params to Param string
 */
fun paramsToParamStr(params: List<Pair<String, String>>): String {
    return params.map { (k, v) -> "${k}=${URLEncoder.encode(v, "utf-8")}" }
        .joinToString("&")
}

fun thousandComma(n: Any): String {
    return NumberFormat.getNumberInstance(Locale.US).format(n)
}
