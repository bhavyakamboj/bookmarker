package com.sivalabs.bookmarker.web.controller

import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.AuthenticationResponse
import com.sivalabs.bookmarker.repo.UserRepository
import com.sivalabs.bookmarker.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class AuthenticationControllerIT : AbstractIntegrationTest() {
    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var existingUser: User

    @BeforeEach
    fun setUp() {
        existingUser = TestHelper.buildUser()
        existingUser = userRepository.save(existingUser)
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll(userRepository.findAllById(listOf(existingUser.id)))
    }

    @Test
    fun `should login successfully`() {
        val responseEntity = authenticate("admin@gmail.com", "admin")
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val userTokenState = responseEntity.body!!
        assertThat(userTokenState.accessToken).isNotNull()
        assertThat(userTokenState.expiresIn).isNotNull()
    }

    @Test
    fun `should get logged in user info`() {
        val request = HttpEntity<String>(getAuthHeaders())
        val responseEntity = restTemplate
                .exchange("/api/me", HttpMethod.GET, request, User::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val user = responseEntity.body!!
        assertThat(user.email).isNotEmpty()
    }

    @Test
    fun `should return unauthorized for not logged in user when get me`() {
        val responseEntity = restTemplate.getForEntity("/api/me", User::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `should refresh auth token`() {
        val request = HttpEntity<String>(getAuthHeaders())
        val responseEntity = restTemplate
                .exchange("/api/auth/refresh", HttpMethod.POST, request, AuthenticationResponse::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val userTokenState = responseEntity.body!!
        assertThat(userTokenState.accessToken).isNotNull()
        assertThat(userTokenState.expiresIn).isNotNull()
    }

    @Test
    fun `should return unauthorized for not logged in user when refresh auth token`() {
        val request = HttpEntity.EMPTY
        val responseEntity = restTemplate.postForEntity("/api/auth/refresh", request, AuthenticationResponse::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
