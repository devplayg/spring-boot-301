package com.devplay.hippo.controller

import com.devplayg.hippo.config.AppConfig
import com.devplayg.hippo.repository.MemberCacheRepo
import com.devplayg.hippo.util.currentUsername
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class RootController(
        private val appConfig: AppConfig,
        private val memberCacheRepo: MemberCacheRepo
) {
    @GetMapping
    fun redirectToLogin(): ModelAndView {
        val auth = SecurityContextHolder.getContext().authentication
        return if (auth is AnonymousAuthenticationToken) {
            ModelAndView("login")
        } else {
            val memberDto = memberCacheRepo.findByUsername(currentUsername())
                    ?: return ModelAndView("redirect:" + appConfig.homeUri)
            
            return ModelAndView("redirect:" + appConfig.homeUri)
        }
    }
}
