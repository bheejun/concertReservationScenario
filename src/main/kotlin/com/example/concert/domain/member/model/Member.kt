package com.example.concert.domain.member.model

import com.example.concert.util.enum.Role
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.UUID

@Entity
@Table(name = "member")
data class Member(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @Column(nullable = false)
    var memberName: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var point: Double,

    @Enumerated(EnumType.STRING)
    var role : Role

)
