package com.example.concert.domain.concert.model

import com.example.concert.domain.reservation.model.Reservation
import com.example.concert.exception.AlreadyBookedException
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "seat")
data class Seat(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    var id: UUID ?= UUID.randomUUID(),

    @Column(nullable = false)
    var seatNum : Int,

    @Column(nullable = false)
    var isBooked : Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    var schedule: Schedule,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    var reservation: Reservation ?= null

) {
    fun booking() {
        if (this.isBooked) {
            throw AlreadyBookedException("${this.seatNum} is already booked. Try reserve another seat")
        }
        this.isBooked = true
    }

}
