package com.example.concert.domain.concert.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "schedule")
data class Schedule(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    var id : UUID? = null,

    @Column(nullable = false)
    var concertDate : LocalDateTime,

    @Column(nullable = false)
    var seatCount : Int,

    @Column(nullable = false)
    var extraSeatCount :Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    var concert : Concert,

    @OneToMany(mappedBy = "schedule", cascade = [CascadeType.ALL], orphanRemoval = true)
    var seat : MutableList<Seat> ?= null,

    @Version
    var version : Long ?= 0



)
