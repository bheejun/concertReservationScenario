package com.example.concert.domain.reservation.model

import com.example.concert.domain.member.model.Member
import com.example.concert.domain.concert.model.Schedule
import com.example.concert.domain.concert.model.Seat
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "reservation")
data class Reservation(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    var id : UUID ? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    var schedule: Schedule,

    @Column(nullable = false)
    var reservationDate : LocalDateTime,

    @Column(nullable = false)
    var payStatus : Boolean = false,

    @Column(nullable = false)
    var cancelStatus : Boolean = false,

    @Column(nullable = false)
    var totalPrice : Double
){
    fun changePaymentStatus(){
        this.payStatus = true
    }
    fun changeCancelStatus(){
        this.cancelStatus = true
    }
}