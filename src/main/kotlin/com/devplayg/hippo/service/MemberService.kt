package com.devplayg.hippo.service

import com.devplayg.hippo.dao.Members
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class MemberService {

    fun all() = transaction {
        Members.selectAll().toList()
    }
}
