package com.example.concert

import org.assertj.core.api.Assertions.assertThat
import com.example.concert.domain.member.dto.request.MemberRegistrationRequestDto
import com.example.concert.domain.member.model.Member
import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.domain.member.service.MemberServiceImpl
import com.example.concert.util.jwt.JwtUtil
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
        memberService = MemberServiceImpl(memberRepository, passwordEncoder, jwtUtil, response)
    }


    @Test
    fun memberRegistrationSuccessTest(){
        // given
        val memberName = "testAccount"
        val password = "TestAccount1!"
        val memberRegistrationRequestDto = MemberRegistrationRequestDto(memberName, password)
        val encodedPassword = "EncodedPassword"

        val member = Member(UUID.randomUUID(), memberName, encodedPassword, 0.00) // You need to replace this with actual constructor
        whenever(memberRepository.existsByMemberName(any())).thenReturn(false)
        whenever(passwordEncoder.encode(any())).thenReturn(encodedPassword)
        whenever(memberRepository.save(member)).thenReturn(null)

        // when
        val result = memberService.memberRegistration(memberRegistrationRequestDto)

        // then
        verify(memberRepository).save(any()) // Verifying the save method was called
        assertThat(result).isEqualTo("Welcome $memberName") // Asserting the expected outcome
    }
}