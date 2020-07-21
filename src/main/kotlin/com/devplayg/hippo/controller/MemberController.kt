package com.devplayg.hippo.controller

import com.devplayg.hippo.entity.MemberDto
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.service.MemberService
import mu.KLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.sql.SQLIntegrityConstraintViolationException

/**
 * Member controller
 */
@Controller
@RequestMapping("/members")
class MemberController(
        val memberService: MemberService
) {
    companion object : KLogging()

    /**
     * displays members page
     */
    @GetMapping("/")
    fun display(model: Model): String {
        return "member/member"
    }

    /**
     * gets all [members]
     */
    @GetMapping
    fun findAll(): ResponseEntity<*> {
        return ResponseEntity(memberService.findAll(), HttpStatus.OK)
    }


    /**
     * gets a member
     */
    @GetMapping("{id}")
    fun findById(@PathVariable id: Long) = try {
            ResponseEntity(memberService.findById(id), HttpStatus.OK)
        } catch(e: ResponseStatusException) {
            ResponseEntity("", e.status)
        }

    /**
     * creates a member
     */
    @PostMapping
    fun create(@ModelAttribute member: MemberDto, bindingResult: BindingResult) = if (bindingResult.hasErrors()) {
            ResponseEntity(bindingResult, HttpStatus.BAD_REQUEST)
        } else try {
            ResponseEntity(memberService.create(member), HttpStatus.OK)
        } catch (e: SQLIntegrityConstraintViolationException) {
            ResponseEntity(e.message, HttpStatus.OK)
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
//        } catch (e: Exception) {
//            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }
