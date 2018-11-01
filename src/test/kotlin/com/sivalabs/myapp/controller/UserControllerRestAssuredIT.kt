package com.sivalabs.myapp.controller

import com.sivalabs.myapp.model.UserDTO
import com.sivalabs.myapp.model.UserProfile
import com.sivalabs.myapp.repo.UserRepository
import com.sivalabs.myapp.utils.TestHelper
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import java.util.Arrays.asList

class UserControllerRestAssuredIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @LocalServerPort
    var serverPort: Int = 0

    lateinit var existingUser: UserDTO
    lateinit var newUser: UserDTO
    lateinit var updateUser: UserDTO

    @Before
    override fun setUp() {
        super.setUp()
        RestAssured.baseURI = "http://localhost:$serverPort"

        newUser = TestHelper.buildUser()

        existingUser = TestHelper.buildUser()
        existingUser = UserDTO.fromEntity(userRepository.save(existingUser.toEntity()))

        updateUser = TestHelper.buildUser()
        updateUser = UserDTO.fromEntity(userRepository.save(updateUser.toEntity()))
    }

    @After
    override fun tearDown() {
        super.tearDown()
        if(userRepository.existsById(newUser.id)) {
            userRepository.deleteById(newUser.id)
        }
        userRepository.deleteAll(userRepository.findAllById(asList<Long>(existingUser.id, updateUser.id)))
    }

    @Test
    fun `should get all users`() {
        val response = given()
                .`when`().get("/api/users")
                .then().statusCode(200)
                .extract().`as`(Array<UserDTO>::class.java)
        val users = asList(*response)
        assertThat(users).isNotEmpty
    }

    @Test
    fun `should get user by id`() {
        mockEngine.mockGetGithubUser(existingUser.githubUsername)
        mockEngine.mockGetGithubUserRepos(existingUser.githubUsername)

        val user = given()
                .`when`()
                .get("/api/users/${existingUser.id}")
                .then()
                .statusCode(200)
                .extract()
                .`as`(UserProfile::class.java)

        assertThat(user).isNotNull
    }

    @Test
    fun `should create user`() {
        val savedUser = given()
                .contentType("application/json")
                .body(newUser)
                .`when`()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .`as`(UserDTO::class.java)
        assertThat(savedUser.id).isNotNull()
    }

    @Test
    fun `should update user`() {
        val updatedUser = given()
                .contentType("application/json")
                .body(updateUser)
                .`when`()
                .put("/api/users/${updateUser.id}")
                .then()
                .statusCode(200)
                .extract()
                .`as`(UserDTO::class.java)

        assertThat(updatedUser.id).isNotNull()
        assertThat(updatedUser.id).isEqualTo(updateUser.id)
        assertThat(updatedUser.email).isEqualTo(updateUser.email)
    }

    @Test
    fun `should delete user`() {
        given().`when`().get("/api/users/${existingUser.id}").then().statusCode(200)
        given().`when`().delete("/api/users/${existingUser.id}").then().statusCode(200)
        given().`when`().get("/api/users/${existingUser.id}").then().statusCode(404)
    }
}
