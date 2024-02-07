package com.example.concert.domain.concert.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
@Table(name = "concert")
data class Concert(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    var id: UUID ?= UUID.randomUUID(),

    @Column(nullable = false)
    var artist : String,

    @Column(nullable = false)
    var concertName : String,

    @Column(nullable = false)
    var ticketPrice : Double
)
