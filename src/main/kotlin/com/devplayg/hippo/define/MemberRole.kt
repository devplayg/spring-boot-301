package com.devplayg.hippo.define

enum class MemberRole(var description: String, var value: Int) {
        Admin("member.level.administrator", 1 shl (10)), // 1024
        Sheriff("member.level.sheriff", 1 shl (9)), // 512
        User("member.level.user", 1 shl (8)) // 256
}

const val SystemMemberId = 0L
