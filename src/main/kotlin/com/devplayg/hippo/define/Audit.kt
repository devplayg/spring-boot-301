package com.devplayg.hippo.define

enum class AuditCategory(val value: Int, val description: String) {
    SignIn(1021, "auth.sign-in"),
    SignInFailure(1022, "auth.sign-in-failure"),
    SignOut(1023, "auth.sign-out"),
    MemberCreated(1011, "member.created"),
    MemberUpdated(1012, "member.updated"),
    MemberDeleted(1013, "member.deleted"),
    APPLICATION_STARTED(1031, "application.started"),
    APPLICATION_STOPPED(1032, "application.stopped"),
    MemberRegistered(9001, "member-registeredj1"),
    UserRolesRevoked(9002, "member-registeredj2"),
}
