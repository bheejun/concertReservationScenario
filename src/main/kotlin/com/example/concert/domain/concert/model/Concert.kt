package com.example.concert.domain.concert.model

import com.example.concert.util.StringListToStringConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
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

    @Convert(converter = StringListToStringConverter::class)
    var seat : List<String> = mutableListOf(),

    @Column(nullable = false)
    var date : String,

    @Column(nullable = false)
    var ticketPrice : Double,

    @Version
    var version : Long ?= 0

)
