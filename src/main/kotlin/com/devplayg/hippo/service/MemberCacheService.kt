package com.devplayg.hippo.service

import com.devplayg.hippo.repository.MemberCacheRepo
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class MemberCacheService(
        private val memberCacheRepo: MemberCacheRepo
) {
    companion object : KLogging()

    fun findOnlineUsernames() = memberCacheRepo.findOnlineUsernames()

    fun findOnlineMembers() = memberCacheRepo.findOnlineUsernames().mapNotNull {
        this.findByUsername(it)
    }

    fun findByUsername(username: String) = memberCacheRepo.findByUsername(username)

    fun markAsOnline(username: String) = memberCacheRepo.markAsOnline(username)

    fun markAsOffline(username: String) = memberCacheRepo.markAsOffline(username)

    fun delete2FA(sessionId: String) = memberCacheRepo.delete2FA(sessionId)

    fun mark2FA(sessionId: String) = memberCacheRepo.mark2FA(sessionId)

    fun check2FA(sessionId: String) = memberCacheRepo.check2FA(sessionId)
}
