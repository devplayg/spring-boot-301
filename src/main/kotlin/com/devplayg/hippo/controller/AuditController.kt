package com.devplayg.hippo.controller

import com.devplayg.hippo.config.AppConfig
import com.devplayg.hippo.entity.filter.AuditFilter
import com.devplayg.hippo.service.AuditService
import groovy.util.logging.Slf4j
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

    @GetMapping
    fun find(@ModelAttribute filter: AuditFilter): ResponseEntity<*> {
        logger.info("# REQ) Request: {}, {}", appConfig.name, appConfig.whatElse)
//        logger.info("# REQ). Request:")



//        logger.debug("startDate: {}", filter.startDate)
//        logger.debug("endDate: {}", filter.endDate)
//        logger.debug("pagingMode: {}", filter.pagingMode)

        return ResponseEntity(auditService.findAll(), HttpStatus.OK)
    }

    @GetMapping("/all")
    fun all() = auditService.findAll()

    @GetMapping("2")
    fun all2(): ResponseEntity<*> {
        return ResponseEntity(auditService.findAll(), HttpStatus.OK)
        //return ResponseEntity.ok(auditService.findAll())
    }
}

