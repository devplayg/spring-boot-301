package com.devplayg.hippo.controller

import com.devplayg.hippo.repository.RedisRepo
import com.devplayg.hippo.service.MemberService
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
        val memberService: MemberService,
        val inMemoryMemberRepo: RedisRepo
//        val memberRepo: MemberRepo
) {
    @GetMapping
    fun list() = memberService.findAll()

    @GetMapping("/mem")
    fun memList() = transaction {
//        Members.selectAll().forEach {
//            inMemoryMemberRepo.save(mapToMemberDto(it))
//        }
        inMemoryMemberRepo.findAll()
    }
}

//    @GetMapping("some")
//    fun listSomething() = memberService.findSomething()
