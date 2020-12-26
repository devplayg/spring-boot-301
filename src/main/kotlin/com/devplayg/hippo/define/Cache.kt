package com.devplayg.hippo.define

import java.time.Duration

val DefaultRedisTimeout: Duration = Duration.ofDays(30)


const val CacheMemberPrefix = "mbr-uname:"
const val CacheMemberIdPrefix = "mbr-id:"
const val CacheMemberOnline = "mbr-online:"
const val CacheMember2FA = "mbr-2fa:"
