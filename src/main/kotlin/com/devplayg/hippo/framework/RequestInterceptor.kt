package com.devplayg.hippo.framework

import com.devplayg.hippo.config.AppConfig
import com.devplayg.hippo.define.NewUsernamePasswordAuthenticationToken
import com.devplayg.hippo.define.authoritiesToValue
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.entity.toMemberMinDto
import com.devplayg.hippo.repository.AssetCacheRepo
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.repository.MemberRepo
import mu.KLogging
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * HandlerInterceptor
 */
@Component
class RequestInterceptor(
        private val memberCacheRepo: MemberCacheRepo,
        private val memberRepo: MemberRepo,
        private val assetCacheRepo: AssetCacheRepo,
        private val appConfig: AppConfig
) : HandlerInterceptor {
    companion object : KLogging()

    /**
     * 요청 전 처리
     */
    override fun preHandle(req: HttpServletRequest, res: HttpServletResponse, dataObject: Any): Boolean {
        /**
         * SecurityContext
         */
        val context = SecurityContextHolder.getContext() ?: return true
        val auth: Authentication = context.authentication


        /**
         * Logging
         */
        logger.debug(
                //"### preHandle: [sign-in:{}] [roles:{}] [sess:{}] [{}-{}] {}{}",
                "### preHandle: [sign-in:{}] [roles:{}] [{}-{}] {}{}",
                auth !is AnonymousAuthenticationToken, // 인증 여부
                authoritiesToValue(auth.authorities), // 권한
                //req.session.id,
                req.method, // Method
                req.remoteAddr, // Remote address
                req.requestURI, // URI
                req.queryString ?: "" // Query string
        )
//        req.parameterMap.forEach { (key, v) ->
//            logger.trace("\t- {} = {}", key, req.parameterMap[key]);
//        }


        /**
         * 사용자 인증이 되지 않은 상태로 받은 요청은 Skip (ex: /login)
         */
        // 로그인 되지 않았다면 skip (Spring Security에서 제어됨)
        if (auth is AnonymousAuthenticationToken) {
             logger.debug("skipped in prehandle: {}", req.requestURI)
            return true
        }


        /**
         * 인증된 사용자인 경우
         */
        // Cache에 있는 사용자 정보를 조회
        var memberMinDtoInCache = memberCacheRepo.findByUsername(auth.name)
        if (memberMinDtoInCache == null) { // Cache 안에 사용자 정보가 없다?
            // 불의의(?) 사고로 인해, Cache가 초기화 된 경우 발생 가능
            //  - ex: redis-cli에서 flushall 실행한 경우 발생 가능
            // 그렇다면 DB에서 다시 사용자 정보를 가져온 후
            val row = memberRepo.findByUsername(auth.name)
            if (row != null) { // DB로부터 조회한 사용자 정보를 Cache에 적재
                memberMinDtoInCache = toMemberMinDto(row)
                memberCacheRepo.save(memberMinDtoInCache)
            } else { // DB에도 사용자 정보가 없다면 "get out of here"
                logger.error(
                        "# === PreHandle: [sign-in:{}][roles:{}][{}-{}] {}{}",
                        auth !is AnonymousAuthenticationToken, // 인증 여부
                        authoritiesToValue(auth.authorities), // 권한
                        req.method, // Method
                        req.remoteAddr, // Remote address
                        req.requestURI, // URI
                        req.queryString ?: "" // Query string
                )
                req.session.invalidate() // 세션 강제 종료
                return false
            }
        }

        // SecurityContext와 Cache에 기록된 사용자 권한 권한 비교
        if (authoritiesToValue(auth.authorities) == memberMinDtoInCache.roles) { // 권한이 동일하면 통과
            return true
        }


        /**
         * 사용자 권한변경이 발생했고, 2FA인증되지 않은 요청이면
         */
        if (!memberCacheRepo.check2FA(req.session.id)) { // 2FA 인증이 되지 았았다면
            if (req.requestURI.startsWith("/2fa") || req.requestURI.startsWith("/login")) { // RSA Token 2FA 인증 요청이면 허용
                return true
            }
            req.session.invalidate() // 세션 강제 종료
            return false
        }


        /**
         * 사용자 권한변경이 발생하면, DB 정보를 기준으로 SecurityContext 업데이트
         */
        val memberInDb = memberRepo.findByUsername(auth.name) ?: return false
        val authentication = NewUsernamePasswordAuthenticationToken(auth, toMemberDto(memberInDb).roles)
        SecurityContextHolder.getContext().authentication = authentication

        return true
    }


    /**
     * 요청 후 처리
     */
    override fun postHandle(req: HttpServletRequest, res: HttpServletResponse, handler: Any, mv: ModelAndView?) {
        if (mv == null) {
            return
        }

        if (handler is HandlerMethod && handler.bean !is RootController) {
            // Get member's timezone and set it to view object
            val auth = SecurityContextHolder.getContext().authentication
            if (auth is AnonymousAuthenticationToken) {
                return
            }

            val controllerName = handler.beanType.simpleName.replace("Controller", "").toLowerCase()
            //String methodName = handlerMethod.getMethod().getName();
            mv.addObject("systemTz", TimeZone.getDefault().toZoneId().id)
            mv.addObject("ctrl", controllerName)
            mv.addObject("remoteAddr", req.remoteAddr)


            val memberDto = memberCacheRepo.findByUsername(auth.name) ?: return
            mv.addObject("memberName", memberDto.name)
            mv.addObject("memberId", memberDto.id)
            mv.addObject("memberUsername", memberDto.username)
            mv.addObject("memberEmail", memberDto.email)
            mv.addObject("memberTimezone", memberDto.timezone)

            mv.addObject("sessionTimeout", appConfig.clientSessionTimeoutSeconds)

            // Assets
            mv.addObject("assets", assetCacheRepo.findAll())
        }
    }


    /**
     * 요청 완료 후 처리
     */
//    override fun afterCompletion(req: HttpServletRequest, res: HttpServletResponse, dataObject: Any, e: Exception?) {
//    }
}
