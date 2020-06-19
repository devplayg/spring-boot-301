package com.devplayg.hippo.define

enum class AuditCategory(val value: Int, val description: String) {
    MemberCreated(1011, "member created"),
    MemberUpdated(1012, "member updated"),
    MemberDeleted(1013, "member deleted"),
    SignIn(1021, "sign in"),
    SignInFailure(1022, "sign in failure"),
    SignOut(1023, "sign out"),
    APPLICATION_STARTED(1031, "application started"),
    APPLICATION_STOPPED(1032, "application stopped"),
}
