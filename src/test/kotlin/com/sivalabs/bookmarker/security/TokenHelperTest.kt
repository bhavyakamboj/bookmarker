package com.sivalabs.bookmarker.security

import com.sivalabs.bookmarker.config.TimeProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils

class TokenHelperTest {
    lateinit var tokenHelper: TokenHelper

    @Before
    fun setUp() {
        tokenHelper = TokenHelper()
        ReflectionTestUtils.setField(tokenHelper, "appName", "Bookmarker")
        ReflectionTestUtils.setField(tokenHelper, "secret", "secret")
        ReflectionTestUtils.setField(tokenHelper, "expiredIn", 604800)
        ReflectionTestUtils.setField(tokenHelper, "authHeader", "Authorization")
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
