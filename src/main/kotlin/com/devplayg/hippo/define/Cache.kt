package com.devplayg.hippo.define

import java.time.Duration

val DefaultRedisTimeout: Duration = Duration.ofDays(30)


const val CacheMemberPrefix = "mbr-uname:"
const val CacheMemberIdPrefix = "mbr-id:"
const val CacheMemberOnline = "mbr-online:"
const val CacheMember2FA = "mbr-2fa:"

/**
 * Spring-boot Redis
 */
const val CacheSpringBootRedisMemberPrefix = "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:"
const val CacheSpringBootRedisSession = "spring:session:sessions:"
const val CacheSpringBootRedisSessionExpire = "spring:session:sessions:expires:"
