package com.example.concert.domain.concert.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
@Table(name = "concert")
data class Concert(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    var id: UUID? = null,

    @Column(nullable = false)
    var artist : String,

    @Column(nullable = false)
    var concertName : String,

    @Column(nullable = false)
    var ticketPrice : Double,

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    var schedule: MutableList<Schedule> ?= null,

    @Version
    var version : Long ?= 0

)
