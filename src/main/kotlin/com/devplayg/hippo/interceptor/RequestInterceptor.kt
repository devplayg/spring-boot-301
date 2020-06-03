package com.devplayg.hippo.interceptor

import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestInterceptor : HandlerInterceptor {
    companion object : KLogging()

    override fun preHandle(req: HttpServletRequest, res: HttpServletResponse, dataObject: Any): Boolean {
//        logger.debug("# REQ) PreHandle: [{} - {}] {}{}", req.method, req.remoteAddr, req.requestURI, req.queryString);
//        if (logger.isDebugEnabled) {

            logger.debug("RequestInterceptor::preHandle() ==========================");
            req.parameterMap.forEach { (key, v) ->
                logger.debug("- {} = {}", key, req.parameterMap[key]);
            }
//        }
        return true
    }

    override fun postHandle(req: HttpServletRequest, res: HttpServletResponse, dataObject: Any, model: ModelAndView?) {
//        logger.debug("# REQ) PostHandle")
    }

    override fun afterCompletion(req: HttpServletRequest, res: HttpServletResponse, dataObject: Any, e: Exception?) {
//        logger.info("# REQ) AfterCompletion")
    }
}
