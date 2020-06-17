package com.devplayg.hippo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("login")
class LoginController {
    @GetMapping(value = ["", "/"])
    fun blog(model: Model): String {
//        model.addAttribute("greeting", "Hello Kotlin with Thymeleaf")
        return "login/login"
    }
}
