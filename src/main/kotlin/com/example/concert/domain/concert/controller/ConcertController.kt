package com.example.concert.domain.concert.controller

import com.example.concert.domain.concert.dto.request.ConcertRegistrationRequestDto
import com.example.concert.domain.concert.dto.response.ConcertResponseDto
import com.example.concert.domain.concert.service.ConcertService
import com.example.concert.util.response.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/concert")
class ConcertController(private val concertService: ConcertService) {

    @PostMapping
    @RequestMapping("/register")
    fun concertRegistration(@RequestBody concertRegistrationRequestDto: ConcertRegistrationRequestDto)
    : ResponseEntity<Response<ConcertResponseDto>>{

        val response = Response(
            status = HttpStatus.OK.value(),
            message = "The concert has been successfully registered",
            data = concertService.registrationConcert(concertRegistrationRequestDto)
        )

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping
    @RequestMapping("/{concertId}")
    fun getConcert(@PathVariable concertId : UUID) : ResponseEntity<Response<ConcertResponseDto>>{
        val response = Response(
            status = HttpStatus.OK.value(),
            message = "Successfully activated",
            data = concertService.getConcert(concertId)
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping
    @RequestMapping("/list")
    fun getConcertList(@PageableDefault(size = 10, sort = ["concertDate"], direction = Sort.Direction.ASC) pageable: Pageable)
    :ResponseEntity<Response<Page<ConcertResponseDto>>>{
        val response =Response(
            status = HttpStatus.OK.value(),
            message = "Successfully got the concert list.",
            data = concertService.getConcertListAfterCurrentTIme(pageable)
        )

        return ResponseEntity(response, HttpStatus.OK)
    }
//
//    @PutMapping
//    @RequestMapping("/{concertId}")
//    fun updateConcert(@PathVariable concertId: UUID, @RequestBody updateRequestDto: ConcertUpdateRequestDto)
//    :ResponseEntity<Response<ConcertUpdateResponseDto>>{
//        val response =Response(
//            status = HttpStatus.OK.value(),
//            message = "Successfully updated.",
//            data = concertService.updateConcert(concertId, updateRequestDto)
//        )
//
//        return ResponseEntity(response, HttpStatus.OK)
//    }

    @DeleteMapping
    @RequestMapping("/delete/{concertId}")
    fun deleteConcert(@PathVariable concertId: UUID):ResponseEntity<Response<String>>{
        val response =Response(
            status = HttpStatus.OK.value(),
            message = "Successfully deleted.",
            data = concertService.deleteConcert(concertId)
        )

        return ResponseEntity(response, HttpStatus.OK)

    }



}