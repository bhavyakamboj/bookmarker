package com.sivalabs.bookmarker.config.security

import com.sivalabs.bookmarker.config.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.servlet.http.HttpServletRequest

@Component
class TokenHelper {

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @Autowired
    private lateinit var timeProvider: TimeProvider

    companion object {
        private const val MILLIS_PER_SECOND = 1000
        private const val AUDIENCE_WEB = "web"
        private const val TOKEN_PREFIX = "Bearer "
        private val signatureAlgorithm = SignatureAlgorithm.HS512
    }

    fun getUsernameFromToken(token: String): String {
        return this.getAllClaimsFromToken(token).subject
    }

    fun refreshToken(token: String): String {
        val claims = this.getAllClaimsFromToken(token)
        claims.issuedAt = timeProvider.now()
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(signatureAlgorithm, applicationProperties.jwt.secret)
            .compact()
    }

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setIssuer(applicationProperties.jwt.issuer)
            .setSubject(username)
            .setAudience(AUDIENCE_WEB)
            .setIssuedAt(timeProvider.now())
            .setExpiration(generateExpirationDate())
            .signWith(signatureAlgorithm, applicationProperties.jwt.secret)
            .compact()
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(applicationProperties.jwt.secret)
            .parseClaimsJws(token)
            .body
    }

    private fun generateExpirationDate(): Date {
        return Date(timeProvider.now().time + applicationProperties.jwt.expiresIn * MILLIS_PER_SECOND)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        return getUsernameFromToken(token) == userDetails.username
    }

    fun getToken(request: HttpServletRequest): String? {
        val authHeader = getAuthHeaderFromHeader(request)
        return if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            authHeader.substring(TOKEN_PREFIX.length)
        } else null
    }

    fun getAuthHeaderFromHeader(request: HttpServletRequest): String? {
        return request.getHeader(applicationProperties.jwt.header)
    }
}
