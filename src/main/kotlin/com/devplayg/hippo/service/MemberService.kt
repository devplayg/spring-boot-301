package com.devplayg.hippo.service

import com.devplayg.hippo.entity.Members
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
                     private val passwordEncoder: PasswordEncoder){
    // : UserDetailsService

////    fun saveAccount(account: Account): Account {
////        account.password = this.passwordEncoder.encode(account.password)
////        return accountRepository.save(account)
////    }
////
//    override fun loadUserByUsername(username: String): UserDetails {
////        redisRepo.findBy
////        return redisRepo.findByEmail(username)?.getAuthorities()
////                ?: throw UsernameNotFoundException("$username Can Not Found")
//    }
}
