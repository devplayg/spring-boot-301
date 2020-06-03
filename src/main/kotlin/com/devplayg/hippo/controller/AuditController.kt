package com.devplayg.hippo.controller

import com.devplayg.hippo.config.AppConfig
import com.devplayg.hippo.entity.filter.AuditFilter
import com.devplayg.hippo.service.AuditService
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/audit")
class AuditController(
        val auditService: AuditService,
        val appConfig : AppConfig
) {
    companion object : KLogging()

    @GetMapping("/")
    fun displayAudit(model: Model): String {
        return "audit/audit"
    }


    /*
        http://127.0.0.1/audit
        http://127.0.0.1/audit?startDate=2020-05-29%2015%3A39&endDate=2020-05-29%2015%3A39
    */
    @GetMapping
    fun find(@ModelAttribute filter: AuditFilter): ResponseEntity<*> {
        logger.debug("AuditController::find() ========================")
        logger.debug("- startDate: {}", filter.startDate)
        logger.debug("- endDate: {}", filter.endDate)
        logger.debug("- pagingMode: {}", filter.pagingMode)
        logger.debug("- dateSearchPeriodDays: {}", filter.dateSearchPeriodDays)

        return ResponseEntity(auditService.findAll(), HttpStatus.OK)
    }

    @GetMapping("/all")
    fun all() = auditService.findAll()

    @GetMapping("2")
    fun all2(): ResponseEntity<*> {
        return ResponseEntity(auditService.findAll(), HttpStatus.OK)
    }
}

