package com.sivalabs.bookmarker.users

import com.sivalabs.bookmarker.common.AbstractIntegrationTest
import com.sivalabs.bookmarker.domain.model.ChangePassword
import com.sivalabs.bookmarker.domain.model.CreateUserRequest
import com.sivalabs.bookmarker.domain.model.UserDTO
import com.sivalabs.bookmarker.domain.service.UserService
import com.sivalabs.bookmarker.domain.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity

class UserControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        httpHeaders = null
    }

    @Test
    fun `should get user by id`() {
        val user = createUser()
        val responseEntity = getUserById(user.id)

        verifyStatusCode(responseEntity, OK)
        val userResult = responseEntity.body
        assertThat(userResult).isNotNull
    }

    @Test
    fun `should create user`() {
        val newUser = UserDTO.fromEntity(TestHelper.buildUser())
        val responseEntity = createUser(newUser)

        verifyStatusCode(responseEntity, CREATED)
        val savedUser = responseEntity.body!!
        assertThat(savedUser.id).isNotNull()
    }

    @Test
    fun `authenticated user should be able to update their details`() {
        val updateUser = createUser()
        val credentials = Pair(updateUser.email, TestHelper.DEFAULT_PASSWORD)
        asAuthenticateUser(credentials)

        updateUser.name = "my new name"
        updateUser(updateUser)

        val responseEntity = getUserById(updateUser.id)
        verifyStatusCode(responseEntity, OK)
        val updatedUser = responseEntity.body!!
        assertThat(updatedUser.id).isEqualTo(updateUser.id)
        assertThat(updatedUser.email).isEqualTo(updateUser.email)
        assertThat(updatedUser.name).isEqualTo(updateUser.name)
    }

    @Test
    fun `admin user should be able to update other user details`() {
        val updateUser = createUser()
        asAuthenticateUser(adminCredentials)

        updateUser.name = "my new name"
        val responseEntity = updateUser(updateUser)

        verifyStatusCode(responseEntity, OK)
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

    @Test
    fun `authenticated user should be able to change password`() {
        val newUser = createUser()
        val credentials = Pair(newUser.email, TestHelper.DEFAULT_PASSWORD)
        asAuthenticateUser(credentials)

        val changePassword =
            ChangePassword(credentials.second, credentials.second + "-new")
        val response = changePassword(changePassword)
        verifyStatusCode(response, OK)

        val authResponse = authenticate(credentials.first, credentials.second)
        verifyStatusCode(authResponse, UNAUTHORIZED)
    }

    @Test
    fun `unauthenticated user should not be able to change password`() {
        val changePassword = ChangePassword(
            user1Credentials.second,
            user1Credentials.second + "-new"
        )
        val response = changePassword(changePassword)
        verifyStatusCode(response, FORBIDDEN)
    }

    private fun changePassword(changePassword: ChangePassword): ResponseEntity<Void> {
        val request = HttpEntity(changePassword, httpHeaders)
        return restTemplate.postForEntity("/api/users/change-password", request, Void::class.java)
    }

    private fun getUserById(id: Long): ResponseEntity<UserDTO> {
        return restTemplate.getForEntity("/api/users/$id", UserDTO::class.java)
    }

    private fun createUser(user: UserDTO): ResponseEntity<UserDTO> {
        val createUserRequest = CreateUserRequest(
            name = user.name,
            email = user.email,
            password = user.password ?: ""
        )
        val request = HttpEntity(createUserRequest)
        return restTemplate.postForEntity("/api/users", request, UserDTO::class.java)
    }

    private fun updateUser(user: UserDTO): ResponseEntity<UserDTO> {
        val request = HttpEntity(user, httpHeaders)
        return restTemplate.exchange("/api/users/${user.id}", PUT, request, UserDTO::class.java)
    }

    private fun deleteUserById(id: Long): ResponseEntity<Void> {
        val request = HttpEntity(null, httpHeaders)
        return restTemplate.exchange("/api/users/$id", DELETE, request, Void::class.java)
    }

    private fun createUser(): UserDTO {
        val user = TestHelper.buildUser()
        return userService.createUser(UserDTO.fromEntity(user))
    }
}
