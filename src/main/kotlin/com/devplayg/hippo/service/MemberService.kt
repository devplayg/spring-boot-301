package com.devplayg.hippo.service

import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.framework.CustomUserDetails
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.repository.MemberRepo
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberService(
        // private val memberCacheRepo: MemberCacheRepo
        private val memberRepo: MemberRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepo.findByUsername(username)?.let{CustomUserDetails.from(toMemberDto(it)) }
                ?: throw UsernameNotFoundException("$username Can Not Found")

//        val m = memberRepo.findByUsername(username)
//        return memberRepo.findByUsername(username)?.let { CustomUserDetails.from(it) }
                ?: throw UsernameNotFoundException("$username Can Not Found")
    }

    fun findAll() = transaction{
        Members.selectAll().map {
            toMemberDto(it)
        }
    }

    fun increaseFailedLoginCount(username: String) = transaction {
        Members.update({ Members.username eq username }) {
            with(SqlExpressionBuilder) {
                it.update(Members.failedLoginCount, Members.failedLoginCount + 1)
            }
        }
    }

//    fun findAllWith() = transaction{
//        Members.selectAll().map {
//            toMemberPublicDto(it)
//        }
//    }
}


//@Service
//class MemberService(
//        private val passwordEncoder: PasswordEncoder,
//        private val memberCacheRepo: MemberCacheRepo
//) : UserDetailsService {
//
//
//    ////    fun saveAccount(account: Account): Account {
//////        account.password = this.passwordEncoder.encode(account.password)
//////        return accountRepository.save(account)
//////    }
//////
//    override fun loadUserByUsername(username: String): UserDetails {
////        return memberCacheRepo.findByUsername(username) ?: throw UsernameNotFoundException(username)
//        return memberCacheRepo.findByUsername(username)
//                ?.let { CustomUserDetails.from(it) }
//                ?: throw UsernameNotFoundException("$username Can Not Found")
//
//
////        return memberCacheRepo.findByUsername(username)?.()
////            ?: throw UsernameNotFoundException(username)
////        return redisRepo.findByEmail(username)?.getAuthorities()
////                ?: throw UsernameNotFoundException("$username Can Not Found")
//    }
//}
