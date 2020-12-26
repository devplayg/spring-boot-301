package com.devplayg.hippo.define

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils

const val SystemMemberId = 0L
const val MemberRolePrefix = "ROLE_"
enum class MemberRole(var description: String, var value: Int) : GrantedAuthority {
    Admin("member.level.administrator", 1 shl (10)), // 1024
    Sheriff("member.level.sheriff", 1 shl (8)), // 256
    User("member.level.user", 1 shl (0)); // 1

    override fun getAuthority(): String {
        return name
    }
}

fun getDefensiveMemberRoles(): Int {
    var roles = 0
    MemberRole.values().filter{
        it.value != MemberRole.Admin.value
    }.forEach{
        roles = (roles or it.value)
    }
    return roles
}

fun authoritiesToValue(authorities: Collection<GrantedAuthority>): Int {
    var authRoleValue = 0
    authorities.forEach {
        val roleName = it.authority.removePrefix(MemberRolePrefix)

        try {
            val role = MemberRole.valueOf(roleName).value
            authRoleValue = authRoleValue or role
        } catch(e: IllegalArgumentException) {
        }
    }
    return authRoleValue
}

fun NewUsernamePasswordAuthenticationToken(auth: Authentication, roles: Int): Authentication {
    return UsernamePasswordAuthenticationToken(
            auth.principal,
            null,
            AuthorityUtils.commaSeparatedStringToAuthorityList(
                    MemberRole.values()
                            .filter { r -> r.value and roles > 0 }
                            .map {
                                MemberRolePrefix + it.name
                            }.joinToString(",")
            )
    )
}
