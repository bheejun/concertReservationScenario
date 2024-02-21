package com.example.concert.domain.member.controller

import com.example.concert.domain.member.dto.request.AdminRegistrationRequestDto
import com.example.concert.domain.member.dto.request.MemberLoginRequestDto
import com.example.concert.domain.member.dto.request.MemberRegistrationRequestDto
import com.example.concert.domain.member.service.MemberService
import com.example.concert.util.response.Response
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService) {

    @PostMapping
    @RequestMapping("/register")
    fun memberRegistration(@Valid @RequestBody memberRegistrationRequestDto: MemberRegistrationRequestDto)
    : ResponseEntity<Response<String>>{

        val response = Response(
            status = HttpStatus.OK.value(),
            message = null,
            data = memberService.memberRegistration(memberRegistrationRequestDto)
        )
        return ResponseEntity(response, HttpStatus.OK)

    }

    @PostMapping
    @RequestMapping("/register/admin")
    fun adminRegistration(@Valid @RequestBody adminRegistrationRequestDto: AdminRegistrationRequestDto)
    : ResponseEntity<Response<String>>{
        val response = Response(
            status = HttpStatus.OK.value(),
            message = null,
            data = memberService.adminRegistration(adminRegistrationRequestDto)
        )
        return ResponseEntity(response, HttpStatus.OK)

    }

    @PostMapping
    @RequestMapping("/login")
    fun memberLogin(@RequestBody memberLoginRequestDto: MemberLoginRequestDto)
    : ResponseEntity<Response<String>>{
        val response = Response(
            status = HttpStatus.OK.value(),
            message = null,
            data = memberService.memberLogin(memberLoginRequestDto)
        )

        return ResponseEntity(response, HttpStatus.OK)
    }
}