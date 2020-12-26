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
        private val memberService: MemberService,
        private val homeUri: String,
        private val tokenRsaEnabled: Boolean
) : SavedRequestAwareAuthenticationSuccessHandler() {
    companion object : KLogging()

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(req: HttpServletRequest, res: HttpServletResponse, auth: Authentication) {

        memberService.allowLogin(auth.name, req.session.id)

        when (auth.principal) {
            /**
             * LDAP인증 로그인인 경우
             */
            is LdapUserDetailsImpl -> {
                this.onLdapAuthenticationSuccess(req, res, auth)
            }


            /**
             * Local DB인증 로그인인 경우
             */
            is CustomUserDetails -> {
                this.onCustomUserAuthenticationSuccess(req, res, auth)
            }
            
            /**
             * 그외
             */
            else -> {
                return
            }
        }
    }


    /**
     * LDAP 사용자 인증 성공 시
     */
    fun onLdapAuthenticationSuccess(req: HttpServletRequest, res: HttpServletResponse, auth: Authentication) {

        /**
         * 2FA 인증을 사용하면
         */
        if (tokenRsaEnabled) {
            redirectStrategy.sendRedirect(req, res, "/tokenrsa/")// RSA-Token인증 페이지로 리다이렉트
            return
        }


        /**
         * 2FA 인증을 사용하지 않으면
         */
        memberService.syncMember(req.session.id) // DB에 기록된 사용자 권한을 Cache로 Overwrite
        val redirectUrl = req.session.getAttribute("prevPage") // 이전 페이지 정보 조회
        if (redirectUrl != null) { // 세션에 Redirection URL 값이 있으면
            req.session.removeAttribute("prevPage");
            redirectStrategy.sendRedirect(req, res, homeUri);
            return
        }


        /**
         * 세션에 Redirection URL 값이 없으면
         */
        super.onAuthenticationSuccess(req, res, auth); // Cache에 저장되 URL로 redirect
    }


    /**
     * DB 사용자 인증 성공 시
     */
    fun onCustomUserAuthenticationSuccess(req: HttpServletRequest, res: HttpServletResponse, auth: Authentication) {
        memberService.handleLocalLogin(auth) // 로그인 처리

        val session: HttpSession = req.session
        val redirectUrl = session.getAttribute("prevPage") // 이전 페이지 정보 조회
        if (redirectUrl != null) { // 세션에 Redirection URL 값이 있으면
            session.removeAttribute("prevPage");
            redirectStrategy.sendRedirect(req, res, homeUri);
            return
        }

        /**
         * 세션에 Redirection URL 값이 없으면
         */
        super.onAuthenticationSuccess(req, res, auth); // Cache에 저장되 URL로 redirect
    }
}
