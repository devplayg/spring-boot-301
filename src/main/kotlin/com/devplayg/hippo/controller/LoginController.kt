package com.devplayg.hippo.controller

import com.devplayg.hippo.config.AppConfig
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("login")
class LoginController(
        private val appConfig: AppConfig
) {
    @GetMapping(value = ["", "/"])
    fun blog(model: Model): String {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return if (auth.principal is UserDetails) { // 이미 로그인 된 상태면
            "redirect:" + appConfig.homeUri
        } else {
            "login/login"
        }
    }
}
