package com.devplayg.hippo.interceptor

import com.devplayg.hippo.framework.CustomUserDetails
import mu.KLogging
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class RequestInterceptor : HandlerInterceptor {
    companion object : KLogging()

    override fun preHandle(req: HttpServletRequest, res: HttpServletResponse, dataObject: Any): Boolean {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        logger.debug("# ================= PreHandle: [sign-in:{}][{}-{}] {}{}",!(auth is AnonymousAuthenticationToken), req.method, req.remoteAddr, req.requestURI, req.queryString?:"")
//        if (logger.isDebugEnabled) {
//        logger.debug("#     - [{} - {}] {}{}==========================", req.method, req.remoteAddr, req.requestURI, req.queryString)
        req.parameterMap.forEach { (key, v) ->
            logger.debug("- {} = {}", key, req.parameterMap[key]);
        }
//        }

//        logger.debug("# sign-in: {}", auth is AnonymousAuthenticationToken)
//        if (auth is AnonymousAuthenticationToken) {
//            logger.debug("# signin: false [{} / {}]", req.method, req.requestURI)
//            return true
//        } else {
//            logger.debug("# signin: true [{} / {}]", req.method, req.requestURI)
//        }
        return true
    }

    override fun postHandle(req: HttpServletRequest, res: HttpServletResponse, handler: Any, mv: ModelAndView?) {
        if (mv == null) {
            return
        }

        if (handler is HandlerMethod) {
            val controllerName = handler.beanType.simpleName.replace("Controller", "").toLowerCase()
            //String methodName = handlerMethod.getMethod().getName();
            mv.addObject("systemTz", TimeZone.getDefault().toZoneId().id)
            mv.addObject("ctrl", controllerName)
            mv.addObject("remoteAddr", req.remoteAddr)

            // Get member'ㄴ timezone and set it to view object
            val auth = SecurityContextHolder.getContext().authentication
            if (auth is AnonymousAuthenticationToken) {
                return
            }
            val member: CustomUserDetails = SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
            mv.addObject("memberName", member.name)
            mv.addObject("memberUsername", member.username)
            mv.addObject("memberTimezone", member.timezone)
        }

//        logger.debug("# REQ) PostHandle")
    }

    override fun afterCompletion(req: HttpServletRequest, res: HttpServletResponse, dataObject: Any, e: Exception?) {
//        logger.info("# REQ) AfterCompletion")
    }
}
