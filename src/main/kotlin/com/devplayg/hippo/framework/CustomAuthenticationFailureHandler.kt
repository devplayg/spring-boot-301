package com.devplayg.hippo.framework;

import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.repository.MemberRepo
import com.devplayg.hippo.service.MemberService
import com.devplayg.hippo.util.auditLog
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
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(w: HttpServletRequest, r: HttpServletResponse, exception: AuthenticationException) {
        val username: String = w.getParameter("app_username")

        // Audit
        auditLog(0, AuditCategory.SignInFailure.value, hashMapOf(Pair("username", username), Pair("exception", exception)))

        // Increase failed login count
        memberService.increaseFailedLoginCount(username)

        // Redirect
        r.sendRedirect("/login?error")
    }
}
