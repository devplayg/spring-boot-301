package com.devplayg.hippo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping("/")
    fun blog(model: Model): String {
        model.addAttribute("greeting", "Hello Kotlin with Thymeleaf")
        return "login/login"
    }
}
