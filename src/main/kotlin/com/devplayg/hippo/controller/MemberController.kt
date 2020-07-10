package com.devplayg.hippo.controller

import com.devplayg.hippo.filter.AuditFilter
import com.devplayg.hippo.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/members")
class MemberController(
        val memberService: MemberService
) {
    @GetMapping("/")
    fun display(model: Model): String {



        return "member/member"
    }

    @GetMapping()
    fun find() : ResponseEntity<*> {
        return ResponseEntity(memberService.findAll(), HttpStatus.OK)
    }
}

//    @GetMapping
//    fun list() = memberService.findAll()

//    @GetMapping("/mem")
//    fun memList() = transaction {
//        Members.selectAll().forEach {
//            inMemoryMemberRepo.save(mapToMemberDto(it))
//        }
//        inMemoryMemberRepo.findAll()
//    }
//}

//    @GetMapping("some")
//    fun listSomething() = memberService.findSomething()
