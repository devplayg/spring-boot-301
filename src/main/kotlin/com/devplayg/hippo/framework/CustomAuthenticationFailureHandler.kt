package com.devplayg.hippo.framework;

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(req: HttpServletRequest, res: HttpServletResponse, exception: AuthenticationException) {
//        val m = HashMap<String, Any>()
//        m["uri"] = req.requestURI
//        m["method"] = req.method
//
//         Save input parameters but password
//        val param = HashMap(req.parameterMap)
//        param["app_password"] = null
//        m["parameter"] = param
//        auditService.audit(AuditCategory.LOGIN_FAILED, m)
//        res.sendRedirect("/login?error&username=" + req.getParameter("app_username"))
    }
}
