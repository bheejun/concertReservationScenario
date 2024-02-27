package com.example.concert.domain.member.service

import com.example.concert.domain.member.dto.AdminRegistrationRequestDto
import com.example.concert.domain.member.dto.MemberLoginRequestDto
import com.example.concert.domain.member.dto.MemberRegistrationRequestDto

interface MemberService {

    fun memberRegistration (memberRegistrationRequestDto: MemberRegistrationRequestDto) :String
    fun memberLogin(memberLoginRequestDto: MemberLoginRequestDto) : String
    fun adminRegistration(adminRegistrationRequestDto: AdminRegistrationRequestDto) : String

}