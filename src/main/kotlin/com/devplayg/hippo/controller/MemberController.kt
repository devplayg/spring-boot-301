package com.devplayg.hippo.controller

import com.devplayg.hippo.filter.AuditFilter
import com.devplayg.hippo.service.MemberService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/members")
class MemberController(
        val memberService: MemberService
//        val inMemoryMemberRepo: RedisRepo
//        val memberRepo: MemberRepo
) {
    @GetMapping("/")
    fun display(@ModelAttribute filter: AuditFilter, model: Model): String {
        return "member/member"
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
