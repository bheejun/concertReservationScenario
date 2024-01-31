package com.example.concert.domain.concert.model

import com.example.concert.domain.concert.model.Schedule
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
@Table(name = "seat")
data class Seat(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    var id : UUID? = null,

    @Column(nullable = false)
    var seatNum : Int,

    @Column(nullable = false)
    var isBooked : Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    var schedule: Schedule,

    @Version
    var version : Long ?= 0

)
