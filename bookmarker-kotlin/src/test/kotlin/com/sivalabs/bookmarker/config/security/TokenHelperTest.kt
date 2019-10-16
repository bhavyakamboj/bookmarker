package com.sivalabs.bookmarker.config.security

import com.sivalabs.bookmarker.config.BookmarkerProperties
import com.sivalabs.bookmarker.config.TimeProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.test.util.ReflectionTestUtils

class TokenHelperTest {
    lateinit var tokenHelper: TokenHelper

    @BeforeEach
    fun setUp() {
        tokenHelper = TokenHelper()
        val bookmarkerProperties = BookmarkerProperties()
        bookmarkerProperties.jwt.secret = "secret"
        bookmarkerProperties.jwt.expiresIn = 604800
        bookmarkerProperties.jwt.header = "Authorization"
        ReflectionTestUtils.setField(tokenHelper, "bookmarkerProperties", bookmarkerProperties)
        ReflectionTestUtils.setField(tokenHelper, "timeProvider", TimeProvider())
    }

    @Test
    fun `should generate auth token`() {
        val token = tokenHelper.generateToken("siva@gmail.com")
        assertThat(token).isNotNull()
        val username = tokenHelper.getUsernameFromToken(token)
        assertThat(username).isEqualTo("siva@gmail.com")
    }
}
