package com.devplayg.hippo.util

import com.devplayg.hippo.framework.CustomUserDetails
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder


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
