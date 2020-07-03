package com.devplayg.hippo.service

import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.entity.toMemberSecuredDto
import com.devplayg.hippo.framework.CustomUserDetails
import com.devplayg.hippo.repository.MemberCacheRepo
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberService(
        private val memberCacheRepo: MemberCacheRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return memberCacheRepo.findByUsername(username)?.let { CustomUserDetails.from(it) }
                ?: throw UsernameNotFoundException("$username Can Not Found")
    }

    fun findAll() = transaction{
        Members.selectAll().map {
            toMemberDto(it)
        }
    }
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
