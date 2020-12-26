package com.devplayg.hippo.framework

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomLogoutHandler(
        private val memberCacheService: MemberCacheService
): LogoutHandler  {
    override fun logout(req: HttpServletRequest, res: HttpServletResponse, auth: Authentication) {
        memberCacheService.markAsOffline(auth.name)
        memberCacheService.delete2FA(req.session.id)
    }
}
