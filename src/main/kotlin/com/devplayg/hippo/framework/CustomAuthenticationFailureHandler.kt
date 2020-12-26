package com.devplayg.hippo.framework;

import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.define.SystemMemberId
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.repository.MemberRepo
import com.devplayg.hippo.service.MemberService
import com.devplayg.hippo.util.auditLog
import mu.KLogging
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFailureHandler(
        val memberService: MemberService
) : AuthenticationFailureHandler {
    companion object : KLogging()

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(w: HttpServletRequest, r: HttpServletResponse, e: AuthenticationException) {
        val username: String = w.getParameter("tkdydwkdkdlel")
        val password: String = w.getParameter("tkdydwkqlalfqjsgh")

        // 로그인 실패 기록
        /*
         * wondory: customUserDetail로 로그인 실패 시, "java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"" 에러 핸들링 필요
         * wondory: 유효하지 않은 ID 로그인 예외처리
         */
        logger.error("login failure; username={}, input={}, inputLen={}, ip={}, message={}", username, password, password.length, w.remoteAddr?:"", e.message?:"")

        // Audit
        val msg = hashMapOf(
                Pair("username", username),
                Pair("message", e.message?:"")
        )
        auditLog(SystemMemberId, AuditCategory.SignInFailure.value, msg)

        // Increase failed login count
        // wondory: last_failed_login 필드 업데이트
        memberService.denyLogin(username)

        // Redirect
        r.sendRedirect("/login?error")
    }
}
