package com.devplayg.hippo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("dashboard")
class DashboardController {
    @GetMapping("b")
    fun displayDashboard(model: Model): String {
        return "log/log"
    }
}
