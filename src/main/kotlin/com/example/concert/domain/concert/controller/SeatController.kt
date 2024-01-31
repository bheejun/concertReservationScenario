package com.example.concert.domain.concert.controller

import com.example.concert.domain.concert.dto.response.SeatStatusResponseDto
import com.example.concert.domain.concert.service.SeatService
import com.example.concert.util.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/concert/seat")
class SeatController(private val seatService: SeatService) {

    @GetMapping
    @RequestMapping("/{scheduleId}")
    fun getSeatStatus(@PathVariable scheduleId : UUID) : ResponseEntity<Response<SeatStatusResponseDto>>{
        val response =Response(
            status = HttpStatus.OK.value(),
            message = "Successfully got the seat information",
            data = seatService.getSeatStatus()
        )

        return ResponseEntity(response, HttpStatus.OK)
    }
}