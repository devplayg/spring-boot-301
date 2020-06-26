package com.devplayg.hippo.controller

import com.devplayg.hippo.define.MemberRole
import com.devplayg.hippo.define.PagingMode
import com.devplayg.hippo.filter.AuditFilter
import com.devplayg.hippo.service.AuditService
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("/audits")
class AuditController(
        private val auditService: AuditService
) {
    companion object : KLogging()

    @RequestMapping(value = ["/"], method = [RequestMethod.GET, RequestMethod.POST])
    fun display(@ModelAttribute filter: AuditFilter, model: Model): String {
        filter.tune()
        model.addAttribute("filter", filter)
        filter.debug(this.javaClass.name + "::displayAudit()")

        logger.debug("{}, {}, {}", MemberRole.Admin.value, MemberRole.Admin.description, MemberRole.Admin.name)
        return "audit/audit"
    }

    /*
        http://127.0.0.1/audit
        http://127.0.0.1/audit?startDate=2020-04-01%2000%3A00&endDate=2020-05-29%2023%3A59
    */
    @GetMapping
    fun find(@ModelAttribute filter: AuditFilter): ResponseEntity<*> {
        filter.tune()
        filter.debug(this.javaClass.name + "::find()")
        if (filter.pagingMode == PagingMode.FastPaging.value) {
            return ResponseEntity(auditService.getAudits(filter), HttpStatus.OK)
        }
        return ResponseEntity(auditService.getAuditsWithTotal(filter), HttpStatus.OK)
    }
}

