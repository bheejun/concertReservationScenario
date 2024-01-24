package com.example.concert.domain.member.controller

import com.example.concert.domain.member.dto.request.MemberLoginRequestDto
import com.example.concert.domain.member.dto.request.MemberRegistrationRequestDto
import com.example.concert.domain.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService) {

    @PostMapping
    @RequestMapping("/registration")
    fun memberRegistration(@Valid @RequestBody memberRegistrationRequestDto: MemberRegistrationRequestDto)
    : ResponseEntity<String>{
        memberService.memberRegistration(memberRegistrationRequestDto)
        return ResponseEntity("Member registration is completed", HttpStatus.OK)

    }

    @GetMapping
    @RequestMapping("/login")
    fun memberLogin(@RequestBody memberLoginRequestDto: MemberLoginRequestDto) : ResponseEntity<String>{
        memberService.memberLogin(memberLoginRequestDto)
        return ResponseEntity("Login complete", HttpStatus.OK)
    }
}