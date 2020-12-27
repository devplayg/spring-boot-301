package com.devplayg.hippo.repository

import com.devplayg.hippo.define.*
import com.devplayg.hippo.entity.MemberMinDto
import com.devplayg.hippo.entity.json
import com.devplayg.hippo.util.currentUsername
import mu.KLogging
import org.jetbrains.kotlin.com.google.gson.Gson
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class MemberCacheRepo(
        private val redisTemplate: RedisTemplate<String, Any>
) {
    companion object : KLogging()

    /**
     * 사용자 저장
     */
    fun save(memberMinDto: MemberMinDto) {
        val m: String = memberMinDto.json()
        redisTemplate.opsForValue().set(CacheMemberPrefix + memberMinDto.username, m, DefaultRedisTimeout)
        redisTemplate.opsForValue().set(CacheMemberIdPrefix + memberMinDto.id, memberMinDto.username, DefaultRedisTimeout)
    }


    /**
     * 사용자 조회 by username
     */
    fun findByUsername(username: String): MemberMinDto? {
        val json = redisTemplate.opsForValue().get(CacheMemberPrefix + username) ?: return null
        //        val json: String = redisTemplate.opsForValue().get(CacheMemberPrefix + username) ?: return ""
        return Gson().fromJson(json.toString(), MemberMinDto::class.java)
    }


    /**
     * Username to Member ID
     */
    fun usernameToMemberId(username: String): Long {
        val json = redisTemplate.opsForValue().get(CacheMemberPrefix + username) ?: return 0
        val member = Gson().fromJson(json.toString(), MemberMinDto::class.java)
        return member.id
    }


    /**
     * 현재 세션의 사용자 ID
     */
    fun currentMemberId(): Long {
        return this.usernameToMemberId(currentUsername())
    }


    /**
     * 사용자 조회 by Member ID
     */
    fun findById(id: Long): MemberMinDto? {
        val username = redisTemplate.opsForValue().get(CacheMemberIdPrefix + id) ?: return null
        return this.findByUsername(username.toString())
    }


    /**
     * 온라인 사용자 조회
     */
    fun findOnlineUsernames() = redisTemplate.keys("$CacheMemberOnline*").map {
        it.removePrefix(CacheMemberOnline)
    }


    /**
     * 온라인 사용자 조회
     */
    fun findOnlineUsernameMap(): MutableMap<String, Boolean> {
        val m: MutableMap<String, Boolean> = hashMapOf()
        this.findOnlineUsernames().forEach {
            m[it] = true
        }
        return m
    }


    /**
     * 온라인으로 표시
     */
    fun markAsOnline(username: String) = redisTemplate.opsForValue().set(
            CacheMemberOnline + username,
            "",
            Duration.ofSeconds(60.toLong())
    )


    /**
     * 오프라인으로 표시
     */
    fun markAsOffline(username: String) = redisTemplate.delete(CacheMemberOnline + username)


    /**
     * Spring 세션 조회
     */
    fun findSpringSessionsByUsername(username: String) = redisTemplate.opsForSet().members(CacheSpringBootRedisMemberPrefix + username)


    /**
     * Spring 세션 삭제
     */
    fun deleteSpringSessionsByKey(key: String) {
        redisTemplate.delete(CacheSpringBootRedisSession + key)
        redisTemplate.delete(CacheSpringBootRedisSessionExpire + key)
    }


    fun mark2FA(sessionId: String) {
        redisTemplate.opsForValue().set(CacheMember2FA + sessionId, "1", DefaultRedisTimeout)
    }


    fun check2FA(sessionId: String) : Boolean {
        redisTemplate.opsForValue().get(CacheMember2FA + sessionId) ?: return false
        return true
    }


    fun delete2FA(sessionId: String) {
        redisTemplate.delete(CacheMember2FA + sessionId)
    }
}


