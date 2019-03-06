package com.sivalabs.bookmarker.web.controller

import com.sivalabs.bookmarker.common.AbstractIntegrationTest
import com.sivalabs.bookmarker.domain.entity.User
import com.sivalabs.bookmarker.domain.model.AuthenticationResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.OK

class AuthenticationControllerIT : AbstractIntegrationTest() {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `should login successfully`() {
        val responseEntity = authenticate(user1Credentials.first, user1Credentials.second)

        verifyStatusCode(responseEntity, OK)
        val userTokenState = responseEntity.body!!
        assertThat(userTokenState.accessToken).isNotNull()
        assertThat(userTokenState.expiresIn).isNotNull()
    }

    @Test
    fun `should get logged in user info`() {
        val request = HttpEntity<String>(getAuthHeaders())
        val responseEntity = restTemplate.exchange("/api/me", GET, request, User::class.java)

        verifyStatusCode(responseEntity, OK)
        val user = responseEntity.body!!
        assertThat(user.email).isNotEmpty()
    }

    @Test
    fun `should return unauthorized for not logged in user when get me`() {
        val responseEntity = restTemplate.getForEntity("/api/me", User::class.java)
        verifyStatusCode(responseEntity, FORBIDDEN)
    }

    @Test
    fun `should refresh auth token`() {
        val request = HttpEntity<String>(getAuthHeaders())
        val responseEntity = restTemplate
            .exchange("/api/auth/refresh", POST, request, AuthenticationResponse::class.java)

        verifyStatusCode(responseEntity, OK)
        val userTokenState = responseEntity.body!!
        assertThat(userTokenState.accessToken).isNotNull()
        assertThat(userTokenState.expiresIn).isNotNull()
    }

    @Test
    fun `should return unauthorized for not logged in user when refresh auth token`() {
        val request = HttpEntity.EMPTY
        val responseEntity =
            restTemplate.postForEntity("/api/auth/refresh", request, AuthenticationResponse::class.java)
        verifyStatusCode(responseEntity, FORBIDDEN)
    }
}
