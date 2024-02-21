package com.example.concert.domain.payment

import com.example.concert.domain.redis.QueueWithRedisService
import com.example.concert.domain.reservation.repository.ReservationRepository
import com.example.concert.domain.reservation.service.ReservationService
import com.example.concert.exception.AuthenticationFailureException
import com.example.concert.exception.NotFoundException
import com.example.concert.exception.PaymentTimeExceededException
import com.google.gson.Gson
import com.google.gson.JsonObject
import jakarta.transaction.Transactional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


@Service
class PaymentService(
    private val queueWithRedisService: QueueWithRedisService,
    private val reservationRepository: ReservationRepository,
    private val reservationService: ReservationService
) {



    @Transactional
    fun verifyPayment(memberId: String, reservationId : String, amount : Long): Boolean{
        val memberUUID = UUID.fromString(memberId)
        val reservationUUID = UUID.fromString(reservationId)
        val reservation = reservationRepository.findById(reservationUUID).orElseThrow {
            NotFoundException("The Reservation is not found for provided id")
        }
        if(amount.toDouble().equals(reservation.totalPrice)){
            confirmPayment(memberUUID, reservationUUID)
            return true
        }else{
            return false
        }

    }

    fun confirmPayment(memberId: UUID, reservationId: UUID) {
        val reservation = reservationRepository.findById(reservationId).orElseThrow {
            NotFoundException("The Reservation is not found for provided id")
        }
        if(memberId == reservation.member.id){
            throw AuthenticationFailureException("The user does not match the person who made the reservation")
        }

        if(queueWithRedisService.checkPaymentTokenExist(reservationId.toString())){
            reservation.changePaymentStatus()
            reservationRepository.save(reservation)
        }else{
            reservationService.cancelReservation(memberId, reservationId)
            throw PaymentTimeExceededException("This reservation has exceeded the payment timeout")
        }


    }

    fun refundForExpiredReservation(apiKey: String, secretKey: String, reservationId: String){
        val access_token = getToken(apiKey, secretKey)
        val url = URL("https://api.iamport.kr/payments/cancel")
        val conn = url.openConnection() as HttpsURLConnection

        conn.requestMethod = "POST"

        conn.setRequestProperty("Content-type", "application/json")
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty("Authorization", access_token)

        conn.doOutput = true

        val json = JsonObject()
        json.addProperty("merchant_uid", reservationId)
        json.addProperty("reason", "Payment time exceeded")

        val bw = BufferedWriter(OutputStreamWriter(conn.outputStream))
        bw.write(json.toString())
        bw.flush()
        bw.close()

        val br = BufferedReader(InputStreamReader(conn.inputStream))
        br.close()
        conn.disconnect()

    }

    private fun getToken(apiKey: String, secretKey: String): String {
        val url = URL("https://api.iamport.kr/users/getToken")
        val conn = url.openConnection() as HttpsURLConnection

        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")
        conn.doOutput = true

        val json = JsonObject().apply {
            addProperty("imp_key", apiKey)
            addProperty("imp_secret", secretKey)
        }

        conn.outputStream.use { os ->
            BufferedWriter(OutputStreamWriter(os)).use { bw ->
                bw.write(json.toString())
                bw.flush()
            }
        }

        val accessToken = conn.inputStream.use { `is` ->
            BufferedReader(InputStreamReader(`is`)).use { br ->
                val gson = Gson()
                val response = gson.fromJson(br.readLine(), Map::class.java)["response"].toString()
                gson.fromJson(response, Map::class.java)["access_token"].toString()
            }
        }

        conn.disconnect()
        return accessToken
    }

    fun startPaymentCheck(memberId: String, reservationId: String){
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                try {
                    if (queueWithRedisService.checkPaymentTokenExist(reservationId)) {
                        delay(305000)
                    } else {
                        reservationService.cancelReservation(UUID.fromString(memberId), UUID.fromString(reservationId))
                        break
                    }
                } catch (e: AuthenticationFailureException) {
                    throw e
                }
            }
        }

    }


}