package com.example.concert.domain.concert.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "schedule")
data class Schedule(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    var id: UUID ?= UUID.randomUUID(),

    @Column(nullable = false)
    var concertDate : LocalDateTime,

    @Column(nullable = false)
    var seatCount : Int,

    @Column(nullable = false)
    var extraSeatCount :Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    var concert : Concert



)