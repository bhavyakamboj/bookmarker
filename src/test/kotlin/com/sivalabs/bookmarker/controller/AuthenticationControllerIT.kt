package com.sivalabs.bookmarker.controller

import com.sivalabs.bookmarker.entity.User
import com.sivalabs.bookmarker.model.AuthenticationRequest
import com.sivalabs.bookmarker.model.UserTokenState
import com.sivalabs.bookmarker.repo.UserRepository
import com.sivalabs.bookmarker.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*

class AuthenticationControllerIT : AbstractIntegrationTest() {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    lateinit var existingUser: User

    @Before
    fun setUp() {
        existingUser = TestHelper.buildUser()
        existingUser = userRepository.save(existingUser)
    }

    @After
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
                .exchange("/api/auth/refresh", HttpMethod.POST, request, UserTokenState::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val userTokenState = responseEntity.body!!
        assertThat(userTokenState.accessToken).isNotNull()
        assertThat(userTokenState.expiresIn).isNotNull()
    }

    @Test
    fun `should return unauthorized for not logged in user when refresh auth token`() {
        val request = HttpEntity.EMPTY
        val responseEntity = restTemplate.postForEntity("/api/auth/refresh", request, UserTokenState::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    private fun getAuthHeaders(): HttpHeaders {
        val responseEntity = authenticate("admin@gmail.com", "admin")
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer ${responseEntity.body?.accessToken}")
        return headers
    }

    private fun authenticate(username: String, password: String): ResponseEntity<UserTokenState> {
        val authenticationRequest = AuthenticationRequest(username, password)
        val request = HttpEntity(authenticationRequest)
        return restTemplate.postForEntity("/api/auth/login", request, UserTokenState::class.java)
    }
}
