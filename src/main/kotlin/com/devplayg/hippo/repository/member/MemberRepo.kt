package com.devplayg.hippo.repository.member

import com.devplayg.hippo.dao.Member
import com.devplayg.hippo.dao.Members
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MemberRepo {
    fun findSomething() = transaction {
        Member.find { Members.id.greater(9) }.toList().map{
            it.toDto()
        }
    }
}
