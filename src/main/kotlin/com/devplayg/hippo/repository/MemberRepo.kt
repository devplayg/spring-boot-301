package com.devplayg.hippo.repository


import com.devplayg.hippo.define.CacheMemberPrefix
import com.devplayg.hippo.entity.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.kotlin.com.google.gson.Gson
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.userdetails.UsernameNotFoundException
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


}


// Memory-cache(Redis)
@Repository
class MemberCacheRepo(
        val redisTemplate: RedisTemplate<String, String>
) {
    fun save(memberDto: MemberDto) {
        val m: String = memberDto.toJson()
        redisTemplate.opsForValue().set(CacheMemberPrefix + memberDto.username, m)
    }

    fun findByUsername(username: String): MemberDto? {
        val member = transaction {
            val m = Members.select { Members.username eq username }
            m.single() ?: return@transaction null

        } ?: return null

        val memberDto = toMemberDto(member)
        return memberDto
    }

//    fun findByUsername(username: String): MemberDto? {
//        val json: String = redisTemplate.opsForValue().get(CacheMemberPrefix + username) ?: return null
//        val memberDto: MemberDto = Gson().fromJson(json, MemberDto::class.java)
//        return memberDto
//    }
}
