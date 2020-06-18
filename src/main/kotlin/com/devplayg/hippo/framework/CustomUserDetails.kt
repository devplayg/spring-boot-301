package com.devplayg.hippo.framework

import com.devplayg.hippo.define.MemberRole
import com.devplayg.hippo.entity.MemberDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class CustomUserDetails private constructor(private val userName: String, private val password: String, private val roles: MutableSet<MemberRole>) : UserDetails {
    companion object {
        fun from(member: MemberDto): CustomUserDetails {
            return with(member) { CustomUserDetails(userName = username, password = "{bcrypt}$password", roles = mutableSetOf(MemberRole.Admin)) }
        }
    }

    override fun getUsername(): String {
        return userName
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}

