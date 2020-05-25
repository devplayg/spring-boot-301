package com.devplayg.hippo.controller

import com.devplayg.hippo.service.AuditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
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

    @GetMapping("2")
    fun all2(): ResponseEntity<*> {
        return ResponseEntity(auditService.findAll(), HttpStatus.OK)
        //return ResponseEntity.ok(auditService.findAll())
    }
}

