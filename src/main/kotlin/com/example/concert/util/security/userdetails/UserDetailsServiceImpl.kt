package com.example.concert.util.security.userdetails

import com.example.concert.domain.member.repository.MemberRepository
import com.example.concert.exception.NotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserDetailsServiceImpl(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(memberId: String?): UserDetails {
        val member = memberRepository
            .findById(
                UUID.fromString(memberId) ?:throw NotFoundException("There is no Member that is related to the memberName"))
            .orElseThrow { throw NotFoundException("There is no Member that is related to the memberName") }

        return UserDetailsImpl(member)


    }
}