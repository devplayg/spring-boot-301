package com.devplayg.hippo.service

import com.devplayg.hippo.dao.Member
import com.devplayg.hippo.repository.MemberRepo
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class MemberService(
        private val memberRepo: MemberRepo
) {
    fun findAll() = transaction {
        Member.all().map { it.toDto() }
    }

    fun findSomething() = memberRepo.findSomething()
}
