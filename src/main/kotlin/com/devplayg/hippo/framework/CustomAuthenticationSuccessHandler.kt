package com.devplayg.hippo.framework

import com.devplayg.hippo.config.AppConfig
import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.util.auditLog
import com.devplayg.hippo.util.currentMember
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationSuccessHandler(
        private val appConfig: AppConfig
) : AuthenticationSuccessHandler {
    protected var redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()
        set

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest,
                                         response: HttpServletResponse, auth: Authentication) {
        auditLog(currentMember()?.id ?: 0, AuditCategory.SignIn.value, hashMapOf(Pair("username", currentMember()?.username ?: "")))
        handle(request, response, auth)
        clearAuthenticationAttributes(request)
    }

    @Throws(IOException::class)
    fun handle(request: HttpServletRequest?,
               response: HttpServletResponse, authentication: Authentication?) {
        if (response.isCommitted) {
//            log.warn("Response has already been committed. Unable to redirect to " + appConfig.homeUri)
            return
        }
        redirectStrategy.sendRedirect(request, response, appConfig.homeUri)
    }

    fun clearAuthenticationAttributes(request: HttpServletRequest) {
        val session = request.getSession(false) ?: return
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }
}
