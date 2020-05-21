package com.devplayg.hippo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("audit")
class AuditController {
    @GetMapping("/")
    fun displayAudit(model: Model): String {
        return "audit/audit"
    }
}
