package com.example.concert.domain.reservation.model

import com.example.concert.domain.concert.model.Concert
import com.example.concert.domain.member.model.Member
import com.example.concert.util.StringListToStringConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
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
    @JoinColumn(name = "concert_id")
    var concert : Concert,

    @Convert(converter = StringListToStringConverter::class)
    var seat : List<String> = mutableListOf(),

    @Column(nullable = false)
    var totalPrice : Double,

    @Column(nullable = false)
    var reservationDate : ZonedDateTime ?= ZonedDateTime.now(ZoneId.of("Asia/Seoul"))



)