package com.sivalabs.bookmarker.security

import com.sivalabs.bookmarker.config.BookmarkerProperties
import com.sivalabs.bookmarker.config.TimeProvider
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import java.util.Date

@Component
class TokenHelper {

    @Autowired
    private lateinit var bookmarkerProperties: BookmarkerProperties

    @Autowired
    private lateinit var timeProvider: TimeProvider

    private val signatureAlgorithm = SignatureAlgorithm.HS512

    private val audienceWeb = "web"

    fun getUsernameFromToken(token: String): String {
        return this.getAllClaimsFromToken(token).subject
    }

    fun refreshToken(token: String): String {
        val claims = this.getAllClaimsFromToken(token)
        claims.issuedAt = timeProvider.now()
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(signatureAlgorithm, bookmarkerProperties.jwt.secret)
                .compact()
    }

    fun generateToken(username: String): String {
        return Jwts.builder()
                .setIssuer(bookmarkerProperties.jwt.issuer)
                .setSubject(username)
                .setAudience(audienceWeb)
                .setIssuedAt(timeProvider.now())
                .setExpiration(generateExpirationDate())
                .signWith(signatureAlgorithm, bookmarkerProperties.jwt.secret)
                .compact()
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(bookmarkerProperties.jwt.secret)
                .parseClaimsJws(token)
                .body
    }

    private fun generateExpirationDate(): Date {
        return Date(timeProvider.now().time + bookmarkerProperties.jwt.expiresIn * 1000)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username
    }

    fun getToken(request: HttpServletRequest): String? {
        val authHeader = getAuthHeaderFromHeader(request)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else null
    }

    fun getAuthHeaderFromHeader(request: HttpServletRequest): String? {
        return request.getHeader(bookmarkerProperties.jwt.header)
    }
}
