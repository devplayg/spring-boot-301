package com.devplayg.hippo.repository


import com.devplayg.hippo.define.CacheMemberPrefix
import com.devplayg.hippo.entity.MemberDto
import com.devplayg.hippo.entity.toJson
import org.jetbrains.kotlin.com.google.gson.Gson
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository


@Repository
class MemberRepo


// Memory-cache
@Repository
class MemberCacheRepo(
        val redisTemplate: RedisTemplate<String, String>
) {
    fun save(memberDto: MemberDto) {
        val m: String = memberDto.toJson()
        redisTemplate.opsForValue().set(CacheMemberPrefix + memberDto.username, m)
    }

    fun findByUsername(username: String): MemberDto? {
        val json: String = redisTemplate.opsForValue().get(CacheMemberPrefix + username) ?: return null
        val memberDto: MemberDto = Gson().fromJson(json, MemberDto::class.java)
        return memberDto

    }
}
