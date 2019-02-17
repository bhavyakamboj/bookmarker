package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.common.AbstractIntegrationTest
import com.sivalabs.bookmarker.users.entity.User
import com.sivalabs.bookmarker.users.model.UserDTO
import com.sivalabs.bookmarker.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity

class UserControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        httpHeaders = null
    }

    @Test
    fun `should get user by id`() {
        val responseEntity = getUserById(adminUserId)

        verifyStatusCode(responseEntity, OK)
        val user = responseEntity.body
        assertThat(user).isNotNull
    }

    @Test
    fun `should create user`() {
        val newUser = TestHelper.buildUser()
        val responseEntity = createUser(newUser)

        verifyStatusCode(responseEntity, CREATED)
        val savedUser = responseEntity.body!!
        assertThat(savedUser.id).isNotNull()
    }

    @Test
    fun `should update user`() {
        val updateUser = createUser()

        updateUser(updateUser)

        val responseEntity = getUserById(updateUser.id)
        verifyStatusCode(responseEntity, OK)
        val updatedUser = responseEntity.body!!
        assertThat(updatedUser.id).isEqualTo(updateUser.id)
        assertThat(updatedUser.email).isEqualTo(updateUser.email)
    }

    @Test
    fun `unauthorized user should not be able to delete user`() {
        val existingUser = createUser()

        val responseEntity = deleteUserById(existingUser.id)

        verifyStatusCode(responseEntity, FORBIDDEN)
    }

    @Test
    fun `admin should be able to delete user`() {
        val existingUser = createUser()

        var responseEntity = getUserById(existingUser.id)
        verifyStatusCode(responseEntity, OK)

        asAuthenticateUser(adminCredentials)
        val response = deleteUserById(existingUser.id)
        verifyStatusCode(response, OK)

        responseEntity = getUserById(existingUser.id)
        verifyStatusCode(responseEntity, NOT_FOUND)
    }

    @Test
    fun `normal user should not be able to delete other user`() {
        val existingUser = createUser()
        asAuthenticateUser(user1Credentials)

        val responseEntity = deleteUserById(existingUser.id)

        verifyStatusCode(responseEntity, NOT_FOUND)
    }

    private fun getUserById(id: Long): ResponseEntity<UserDTO> {
        return restTemplate.getForEntity("/api/users/$id", UserDTO::class.java)
    }

    private fun createUser(user: User): ResponseEntity<UserDTO> {
        val request = HttpEntity(user)
        return restTemplate.postForEntity("/api/users", request, UserDTO::class.java)
    }

    private fun updateUser(user: User) {
        val request = HttpEntity(user)
        restTemplate.put("/api/users/${user.id}", request, UserDTO::class.java)
    }

    private fun deleteUserById(id: Long): ResponseEntity<Void> {
        val request = HttpEntity(null, httpHeaders)
        return restTemplate.exchange("/api/users/$id", DELETE, request, Void::class.java)
    }

    private fun createUser(): User {
        return userRepository.save(TestHelper.buildUser())
    }
}
