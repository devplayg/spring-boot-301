package com.devplayg.hippo.repository

import com.devplayg.hippo.entity.Member
import com.devplayg.hippo.entity.Members
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MemberRepo {
    fun findSomething() = transaction {
        Member.find { Members.id.greater(9) }.toList().map {
            it.toDto()

        }
    }
}
