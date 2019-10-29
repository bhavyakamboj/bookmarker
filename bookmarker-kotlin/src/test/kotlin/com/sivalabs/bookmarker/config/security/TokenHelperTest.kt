package com.sivalabs.bookmarker.config.security

import com.sivalabs.bookmarker.config.ApplicationProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.test.util.ReflectionTestUtils

class TokenHelperTest {
    private lateinit var tokenHelper: TokenHelper

    @BeforeEach
    fun setUp() {
        tokenHelper = TokenHelper()
        val applicationProperties = ApplicationProperties()
        applicationProperties.jwt.secret = "secret"
        applicationProperties.jwt.expiresIn = 604800
        applicationProperties.jwt.header = "Authorization"
        ReflectionTestUtils.setField(tokenHelper, "applicationProperties", applicationProperties)
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
