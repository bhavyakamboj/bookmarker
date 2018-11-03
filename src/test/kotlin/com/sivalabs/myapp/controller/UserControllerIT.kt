package com.sivalabs.myapp.controller

import com.sivalabs.myapp.model.UserDTO
import com.sivalabs.myapp.model.UserProfile
import com.sivalabs.myapp.repo.UserRepository
import com.sivalabs.myapp.utils.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import java.util.Arrays.asList

class UserControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    lateinit var existingUser: UserDTO
    lateinit var newUser: UserDTO
    lateinit var updateUser: UserDTO

    @Before
    override fun setUp() {
        super.setUp()
        newUser = TestHelper.buildUser()

        existingUser = TestHelper.buildUser()
        existingUser = UserDTO.fromEntity(userRepository.save(existingUser.toEntity()))

        updateUser = TestHelper.buildUser()
        updateUser = UserDTO.fromEntity(userRepository.save(updateUser.toEntity()))
    }

    @After
    override fun tearDown() {
        super.tearDown()
        if (userRepository.existsById(newUser.id)) {
            userRepository.deleteById(newUser.id)
        }
        userRepository.deleteAll(userRepository.findAllById(asList<Long>(existingUser.id, updateUser.id)))
    }

    @Test
    fun `should get all users`() {
        val responseEntity = restTemplate.getForEntity("/api/users", Array<UserDTO>::class.java)
        val users = asList(*responseEntity.body!!)
        assertThat(users).isNotEmpty
    }

    @Test
    fun `should get user by id`() {
        mockEngine.mockGetGithubUser(existingUser.githubUsername)
        mockEngine.mockGetGithubUserRepos(existingUser.githubUsername)

        val responseEntity = restTemplate.getForEntity("/api/users/${existingUser.id}", UserProfile::class.java)
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
        val responseEntity = restTemplate.getForEntity("/api/users/${updateUser.id}", UserProfile::class.java)
        val updatedUser = responseEntity.body!!
        assertThat(updatedUser.id).isEqualTo(updateUser.id)
        assertThat(updatedUser.email).isEqualTo(updateUser.email)
    }

    @Test
    fun `should delete user`() {
        var responseEntity = restTemplate.getForEntity("/api/users/${existingUser.id}", UserProfile::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(OK)
        restTemplate.delete("/api/users/${existingUser.id}")
        responseEntity = restTemplate.getForEntity("/api/users/${existingUser.id}", UserProfile::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(NOT_FOUND)
    }
}
