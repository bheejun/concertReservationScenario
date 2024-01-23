package com.example.concert.domain.member.service

import com.example.concert.domain.member.dto.request.MemberLoginRequestDto
import com.example.concert.domain.member.dto.request.MemberRegistrationRequestDto

interface MemberService {

    fun memberRegistration (memberRegistrationRequestDto: MemberRegistrationRequestDto) :String
    fun memberLogin(memberLoginRequestDto: MemberLoginRequestDto) : String

}