package com.devplayg.hippo.service

import com.devplayg.hippo.entity.MemberDto
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.framework.CustomUserDetails
import com.devplayg.hippo.repository.MemberRepo
import com.devplayg.hippo.util.SubnetUtils
import mu.KLogging
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class MemberService(
        private val memberRepo: MemberRepo
) : UserDetailsService {
    companion object : KLogging()

    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepo.findByUsername(username)?.let { CustomUserDetails.from(toMemberDto(it, true)) }
                ?: throw UsernameNotFoundException("$username Can Not Found")
    }

    fun findAll() = memberRepo.findAll()

    fun findById(id: Long)= memberRepo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun create(member: MemberDto) {
        val ipList = member.accessibleIpListText.trim().split("\\s+|,+".toRegex())
        val networks = ipList.mapNotNull {
            if (it.isBlank()) {
                return@mapNotNull null
            }
            val  ipCidr = if (it.contains("/")) {
                it
            } else {
                "$it/32"
            }

            SubnetUtils(ipCidr)
            ipCidr
        }
        member.accessibleIpList = networks.toMutableList()
        member.password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(member.password)


        memberRepo.create(member)
        logger.debug("{}", member)
    }


    fun increaseFailedLoginCount(username: String) =  transaction {
        Members.update({ Members.username eq username }) {
            with(SqlExpressionBuilder) {
                it.update(Members.failedLoginCount, Members.failedLoginCount + 1)
            }
        }
    }
}

//                ?. single()
//                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
//    }