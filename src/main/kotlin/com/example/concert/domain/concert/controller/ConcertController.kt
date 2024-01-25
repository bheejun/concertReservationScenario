package com.example.concert.domain.concert.controller

import com.example.concert.domain.concert.dto.request.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.service.ConcertService
import com.example.concert.util.response.Response
import jakarta.validation.constraints.Null
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/concert")
class ConcertController(private val concertService: ConcertService) {

    @PostMapping
    @RequestMapping("/registration")
    fun concertRegistration(@RequestBody concertRegistrationRequestDto: ConcertRegistrationRequestDto)
    : ResponseEntity<Response<String>>{

        val response = Response(
            status = HttpStatus.OK,
            message = null,
            data = concertService.registrationConcert(concertRegistrationRequestDto)
        )

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping
    @RequestMapping("/getConcertList")
    fun getConcertList():ResponseEntity<Response<Page<ConcertResponseDto>>>{
        val response =Response(
            status = HttpStatus.OK,
            message = "Successfully got the concert list.",
            data = concertService.getConcertList()
        )

        return ResponseEntity(response, HttpStatus.OK)
    }


}