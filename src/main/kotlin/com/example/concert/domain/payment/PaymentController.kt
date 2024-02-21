package com.example.concert.domain.payment

import com.example.concert.exception.NotFoundException
import com.example.concert.util.security.userdetails.UserDetailsImpl
import com.siot.IamportRestClient.IamportClient
import com.siot.IamportRestClient.exception.IamportResponseException
import com.siot.IamportRestClient.response.IamportResponse
import com.siot.IamportRestClient.response.Payment
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.io.IOException
import java.util.*


@Controller
class PaymentController(
    @Value("\${imp.apiKey}")
    private val apiKey: String,
    @Value("\${imp.secretKey}")
    private val secretKey: String,
    private val paymentService: PaymentService
) {
    private val iamportClient = IamportClient(apiKey, secretKey)

    @PostMapping("/payment/{reservationId}")
    fun confirmPayment(@PathVariable reservationId : UUID,
                       @AuthenticationPrincipal member : UserDetailsImpl) : ResponseEntity<Boolean>{
        try {
            paymentService.confirmPayment( member.getMemberId(),reservationId)
        }catch (e: RuntimeException){
            paymentService.refundForExpiredReservation(apiKey, secretKey, reservationId.toString())
            throw e
        }
        return ResponseEntity(true, HttpStatus.OK)

    }


    @PostMapping("/payments/complete/{impUid}")
    @Throws(
        IamportResponseException::class,
        IOException::class
    )
    fun verifyPaymentByImpUid(
        model: Model?,
        locale: Locale?,
        session: HttpSession?,
        @PathVariable impUid : String
    ): IamportResponse<Payment> {
        val payment = iamportClient.paymentByImpUid(impUid)
        val memberId = payment.response.customerUid ?: throw (NotFoundException ("memberId field is empty"))
        val reservationId =payment.response.merchantUid ?: throw (NotFoundException ("reservationId field is empty"))
        val amount =payment.response.amount.toLong()

        try {
            paymentService.verifyPayment(memberId, reservationId, amount)
            return payment

        }catch (e: RuntimeException){
            paymentService.refundForExpiredReservation(apiKey, secretKey, reservationId)
            throw e
        }

    }

}