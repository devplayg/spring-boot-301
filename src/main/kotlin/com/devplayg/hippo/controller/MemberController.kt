package com.devplayg.hippo.controller

import com.devplayg.hippo.service.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
        private val memberService: MemberService
) {
    @GetMapping
    fun list() = memberService.findAll()

    @GetMapping("some")
    fun listSomething() = memberService.findSomething()
}
