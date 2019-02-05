package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.common.AbstractIntegrationTest
import com.sivalabs.bookmarker.users.model.UserDTO
import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import java.util.Arrays.asList

class UserControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var existingUser: User
    lateinit var newUser: User
    lateinit var updateUser: User

    @BeforeEach
    fun setUp() {
        newUser = TestHelper.buildUser()

        existingUser = TestHelper.buildUser()
        existingUser = userRepository.save(existingUser)

        updateUser = TestHelper.buildUser()
        updateUser = userRepository.save(updateUser)
    }

    @AfterEach
    fun tearDown() {
        if (userRepository.existsById(newUser.id)) {
            userRepository.deleteById(newUser.id)
        }
        userRepository.deleteAll(userRepository.findAllById(asList<Long>(existingUser.id, updateUser.id)))
    }

    @Test
    fun `should get user by id`() {
        val responseEntity = restTemplate.getForEntity("/api/users/${existingUser.id}", UserDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val user = responseEntity.body
        assertThat(user).isNotNull
    }

    @Test
    fun `should create user`() {
        val request = HttpEntity(newUser)
        val responseEntity = restTemplate.postForEntity("/api/users", request, UserDTO::class.java)
        val savedUser = responseEntity.body!!
        assertThat(savedUser.id).isNotNull()
    }

    @Test
    fun `should update user`() {
        val request = HttpEntity(updateUser)
        restTemplate.put("/api/users/${updateUser.id}", request, UserDTO::class.java)
        val responseEntity = restTemplate.getForEntity("/api/users/${updateUser.id}", UserDTO::class.java)
        val updatedUser = responseEntity.body!!
        assertThat(updatedUser.id).isEqualTo(updateUser.id)
        assertThat(updatedUser.email).isEqualTo(updateUser.email)
    }

    @Test
    fun `unauthorized user should not be able to delete user`() {
        val response = restTemplate.exchange("/api/users/${existingUser.id}", HttpMethod.DELETE, null, Void::class.java)
        assertThat(response.statusCode).isEqualTo(UNAUTHORIZED)
    }

    @Test
    fun `admin should be able to delete user`() {
        var responseEntity = restTemplate.getForEntity("/api/users/${existingUser.id}", UserDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)

        val request = HttpEntity(null, getAuthHeaders("admin@gmail.com", "admin"))
        val response = restTemplate.exchange("/api/users/${existingUser.id}", HttpMethod.DELETE, request, Void::class.java)
        assertThat(response.statusCode).isEqualTo(OK)

        responseEntity = restTemplate.getForEntity("/api/users/${existingUser.id}", UserDTO::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `normal user should not be able to delete other user`() {
        val request = HttpEntity(null, getAuthHeaders("siva@gmail.com", "siva"))
        val response = restTemplate.exchange("/api/users/${existingUser.id}", HttpMethod.DELETE, request, Void::class.java)
        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
    }
}
