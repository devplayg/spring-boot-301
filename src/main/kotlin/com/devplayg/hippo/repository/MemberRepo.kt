package com.devplayg.hippo.repository


import com.devplayg.hippo.define.CacheMemberPrefix
import com.devplayg.hippo.entity.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository


@Repository
class MemberRepo {
    fun findByUsername(username: String) = transaction {
        try {
            Members.select { Members.username eq username }.single()
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun findById(id: Long) = transaction {
        Members.select { Members.id.eq(id) } .map {
            toMemberDto(it)
        }.firstOrNull()
    }

    fun findAll(): List<MemberDto> {
//        var members: List<MemberDto>
        var memberTable = HashMap<Long, MemberDto>()
        transaction {
            Members.selectAll().map {
                memberTable.put(it[Members.id].value, toMemberDto(it))
            }

            MemberAllowedIpList.selectAll().map {
                if (it[MemberAllowedIpList.memberId] in memberTable) {
                    var m = memberTable.get(it[MemberAllowedIpList.memberId])
                    m!!.accessibleIpList!!.add(it[MemberAllowedIpList.ipCidr])

                }
            }

        }
        return memberTable.values.toList()
    }

    fun create(member: MemberDto) = transaction {
        val lastInsertId = Members.insert {
            it[username] = member.username
            it[name] = member.name
            it[email] = member.email
            it[password] = member.password!!
            it[roles] = member.roles
            it[timezone] = member.timezone
            it[failedLoginCount] = failedLoginCount
        } get Members.id


        for (net in member.accessibleIpList!!) {
            MemberAllowedIpList.insert {
                it[memberId] = lastInsertId.value
                it[ipCidr] = net
            }
        }

        member.id = lastInsertId.value
    }
}

