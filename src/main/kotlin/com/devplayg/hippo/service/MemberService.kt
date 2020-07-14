package com.devplayg.hippo.service

import com.devplayg.hippo.entity.Member
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.framework.CustomUserDetails
import com.devplayg.hippo.repository.MemberRepo
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberService(
        private val memberRepo: MemberRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepo.findByUsername(username)?.let { CustomUserDetails.from(toMemberDto(it)) }
                ?: throw UsernameNotFoundException("$username Can Not Found")
    }

    fun findAll() = transaction {
        Members.selectAll().map {
            toMemberDto(it)
        }
    }

    fun create(member: Member) = transaction {
//        Members.insert {
//            it[id] = member.id.table
//            it[username] = member.username
//            it[name] = member.name
//            it[email] = member.email
//            it[password] = member.password
//            it[roles] = member.roles
//            it[timezone] = member.timezone
//        }
        Member.new {
            username = member.username
            name = member.name
            email = member.email
            password = member.password
            roles = member.roles
            timezone = member.timezone
            failedLoginCount = member.failedLoginCount
        }
    }


    fun increaseFailedLoginCount(username: String) = transaction {
        Members.update({ Members.username eq username }) {
            with(SqlExpressionBuilder) {
                it.update(Members.failedLoginCount, Members.failedLoginCount + 1)
            }
        }
    }
}

