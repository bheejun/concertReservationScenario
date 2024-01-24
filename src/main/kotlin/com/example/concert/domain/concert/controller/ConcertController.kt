package com.example.concert.domain.concert.controller

import com.example.concert.domain.concert.dto.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.service.ConcertService
import com.example.concert.domain.concert.service.ConcertServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/concert")
class ConcertController(private val concertService: ConcertService) {

    @PostMapping
    @RequestMapping("/registration")
    fun concertRegistration(@RequestBody concertRegistrationRequestDto: ConcertRegistrationRequestDto) : ResponseEntity<String>{

        return ResponseEntity(concertService.registrationConcert(concertRegistrationRequestDto), HttpStatus.OK)
    }


}