package com.example.concert.domain.member.model

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
    val id : UUID ?= null,

    @Column(nullable = false)
    var memberName : String,

    @Column(nullable = false)
    var password : String,

    @Column(nullable = false)
    var point : Double

//    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
//    val reservationList : String

)
