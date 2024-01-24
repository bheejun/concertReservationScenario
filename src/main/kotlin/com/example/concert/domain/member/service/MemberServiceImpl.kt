package com.example.concert.domain.member.service

import com.example.concert.domain.member.dto.request.MemberLoginRequestDto
import com.example.concert.domain.member.dto.request.MemberRegistrationRequestDto
import com.example.concert.domain.member.model.Member
import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.exception.MemberNameAlreadyExistsException
import com.example.concert.exception.NotFoundException
import com.example.concert.util.jwt.JwtUtil
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val response: HttpServletResponse


) : MemberService {

    @Transactional
    override fun memberRegistration(memberRegistrationRequestDto: MemberRegistrationRequestDto): String {
        val memberName = memberRegistrationRequestDto.memberName
        val encodedPassword = passwordEncoder.encode(memberRegistrationRequestDto.password)

        if (memberRepository.existsByMemberName(memberName)) {
            throw MemberNameAlreadyExistsException("Already used id")
        }

        memberRepository.save(
            Member(
                memberName = memberName,
                password = encodedPassword,
                point = 0.00
            )
        )
        println("${memberName}, ${memberRegistrationRequestDto.password} ,${encodedPassword}")
        return "Welcome ${memberName}"




    }

    @Transactional
    override fun memberLogin(memberLoginRequestDto: MemberLoginRequestDto): String {

        val memberName = memberLoginRequestDto.memberName
        val password = memberLoginRequestDto.password

        if(!memberRepository.existsByMemberName(memberName)){
            throw NotFoundException("Id or password is wrong. Try again")
        }

        val requestMember = memberRepository.findByMemberName(memberName).orElseThrow{
            throw NotFoundException("Id or password is wrong. Try again")
        }

        if (!passwordEncoder.matches(password, requestMember.password)) {
            throw NotFoundException("Id or password is wrong. Try again")
        }

        val generalToken = jwtUtil.generateGeneralToken(memberName)

        response.addHeader("Authorization", "Bearer $generalToken")

        return "Login success"

    }


}