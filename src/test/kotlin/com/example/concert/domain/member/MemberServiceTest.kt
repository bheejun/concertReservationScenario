package com.example.concert.domain.member

import com.example.concert.domain.member.dto.request.AdminRegistrationRequestDto
import org.assertj.core.api.Assertions.assertThat
import com.example.concert.domain.member.dto.request.MemberRegistrationRequestDto
import com.example.concert.domain.member.model.Member
import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.domain.member.service.MemberServiceImpl
import com.example.concert.exception.DuplicateException
import com.example.concert.util.enum.Role
import com.example.concert.util.jwt.JwtUtil
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class MemberServiceTest {

    private lateinit var memberRepository : MemberRepository
    private lateinit var passwordEncoder: BCryptPasswordEncoder
    private lateinit var jwtUtil: JwtUtil
    private lateinit var response : HttpServletResponse
    private lateinit var memberService : MemberServiceImpl

    @BeforeEach
    fun setUp(){
        memberRepository = mock()
        passwordEncoder = mock()
        jwtUtil = mock()
        response = mock()
        memberService = MemberServiceImpl(memberRepository, passwordEncoder, jwtUtil, response, "1234")
    }


    @Test
    fun memberRegistrationSuccessTest(){
        // given
        val memberName = "testAccount"
        val password = "TestAccount1!"
        val memberRegistrationRequestDto = MemberRegistrationRequestDto(memberName, password)
        val encodedPassword = "EncodedPassword"
        val role = Role.USER

        val member = Member(UUID.randomUUID(), memberName, encodedPassword, 0.00, role)
        whenever(memberRepository.existsByMemberName(any())).thenReturn(false)
        whenever(passwordEncoder.encode(any())).thenReturn(encodedPassword)
        whenever(memberRepository.save(member)).thenReturn(null)

        // when
        val result = memberService.memberRegistration(memberRegistrationRequestDto)

        // then
        verify(memberRepository).save(any())
        assertThat(result).isEqualTo("Welcome $memberName")
    }

    @Test
    fun memberRegistrationDuplicateNameTest(){
        //given
        val memberName = "testAccount"
        val password = "TestAccount1!"
        val memberRegistrationRequestDto = MemberRegistrationRequestDto(memberName, password)

        whenever(memberRepository.existsByMemberName(memberName)).thenReturn(true)

        // when/then
        assertThrows<DuplicateException> {
            memberService.memberRegistration(memberRegistrationRequestDto)
        }

        verify(memberRepository, never()).save(any())

    }

    @Test
    fun adminRegistrationSuccessTest(){
        val adminName = "testAdmin1"
        val password = "TestAdmin1!"
        val adminRegistrationRequestDto = AdminRegistrationRequestDto(adminName, password, adminCode = "1234")

        whenever(memberRepository.existsByMemberName(adminName)).thenReturn(false)
    }


}