package com.example.concert.domain.member.repository

import com.example.concert.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface MemberRepository : JpaRepository<Member, UUID> {

    fun existsByMemberName (memberName : String) : Boolean

    fun findByMemberName (memberName: String) : Optional<Member>


}