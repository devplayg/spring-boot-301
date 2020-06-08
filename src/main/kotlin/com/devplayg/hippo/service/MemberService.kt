package com.devplayg.hippo.service

import com.devplayg.hippo.entity.Members
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class MemberService(
//        private val memberRepo: MemberRepo
) {
    fun findAll() = transaction {
        Members.selectAll()
    }

//    fun findSomething() = memberRepo.findSomething()
}
