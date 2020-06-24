package com.devplayg.hippo.framework;

import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.util.auditLog
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(req: HttpServletRequest, res: HttpServletResponse, exception: AuthenticationException) {
        auditLog(0, AuditCategory.SignInFailure.value, hashMapOf(Pair("username", req.getParameter("app_username"))))
        res.sendRedirect("/login?error")

//        val m = HashMap<String, Any>()
//        m["uri"] = req.requestURI
//        m["method"] = req.method
//
//         Save input parameters but password
//        val param = HashMap(req.parameterMap)
//        param["app_password"] = null
//        m["parameter"] = param
//        auditService.audit(AuditCategory.LOGIN_FAILED, m)
    }
}
