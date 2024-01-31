package com.example.concert.domain.concert.controller

import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.dto.response.GetAvailableDateResponseDto
import com.example.concert.domain.concert.service.ScheduleService
import com.example.concert.util.response.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/concert/schedule")
class ScheduleController (private val scheduleService: ScheduleService){

    @GetMapping
    @RequestMapping("/{concertId}/available-date")
    fun getAvailableDatesForSpecificConcert(@PathVariable concertId : UUID)
    :ResponseEntity<Response<GetAvailableDateResponseDto>>{
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully activated",
            data = scheduleService.getAvailableDatesForSpecificConcert(concertId)
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping
    @RequestMapping("/available-concert-list")
    fun getAvailableConcertList(@PageableDefault(size = 10, sort = ["concertDate"], direction = Sort.Direction.ASC) pageable: Pageable)
    :ResponseEntity<Response<Page<ConcertResponseDto>>> {
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully activated",
            data = scheduleService.getAvailableConcertList(pageable)
        )
        return ResponseEntity(response, HttpStatus.OK)

    }
}