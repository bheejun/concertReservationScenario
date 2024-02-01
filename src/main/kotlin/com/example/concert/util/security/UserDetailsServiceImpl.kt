package com.example.concert.util.security

import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.exception.NotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val member = memberRepository
            .findByMemberName(
                username?:throw NotFoundException("There is no Member that is related to the memberName"))
            .orElseThrow { throw NotFoundException("") }

        return UserDetailsImpl(member)


    }
}