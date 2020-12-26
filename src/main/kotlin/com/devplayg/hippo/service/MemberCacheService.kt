package com.devplayg.hippo.service

import com.devplayg.hippo.repository.MemberCacheRepo
import org.springframework.stereotype.Service

@Service
class MemberCacheService(
    private val memberCacheRepo: MemberCacheRepo
) {
    fun aa() = memberCacheRepo.markAsOnline()
    fun aa() = memberCacheRepo.markAsOffline()
    fun aa() = memberCacheRepo.delete2Fa()
}