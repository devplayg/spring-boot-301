package com.devplayg.hippo.controller

import com.devplayg.hippo.service.AuditService
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/audit")
class AuditController(
        val auditService: AuditService
) {
    @GetMapping("/")
    fun displayAudit(model: Model): String {
        return "audit/audit"
    }

    @GetMapping
    fun all() = auditService.findAll()
}

