package com.example.concert.util.jwt
import com.example.concert.util.enum.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service
import java.util.*

@Service
@PropertySource("classpath:application.yml")
class JwtUtil(
    @Value("\${secret-key}")
    private val secretKey: String,
    @Value("\${issuer}")
    private val issuer: String

) {
    private val signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateGeneralToken(username: String, role :Role): String {
        val expirationHours = 2

        return Jwts.builder()
            .setSubject(username)
            .setIssuer(issuer)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationHours * 3600000))
            .signWith(signingKey, SignatureAlgorithm.HS512)
            .claim("role", role.toString())
            .compact()
    }

    fun generateQueueToken(queueTokenDetail: QueueTokenDetail): String{
        val expirationTime = 300000

        return Jwts.builder()
            .setSubject(queueTokenDetail.memberUUID.toString())
            .claim("queuePostion", queueTokenDetail.queuePosition)
            .setIssuer(issuer)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(signingKey, SignatureAlgorithm.HS512)
            .compact()

    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body
            claims.subject
        } catch (e: Exception) {
            null
        }
    }

    fun getUserRoleFromToken(token: String) : String?{

        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body

            claims["role", String::class.java]
        } catch (e: Exception) {
            return null
        }
    }

}