package com.sivalabs.myapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sivalabs.myapp.model.UserDTO
import com.sivalabs.myapp.model.UserProfile
import com.sivalabs.myapp.repo.UserRepository
import com.sivalabs.myapp.utils.TestHelper
import io.restassured.RestAssured
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort

import io.restassured.RestAssured.given
import java.util.Arrays.asList
import org.assertj.core.api.Assertions.assertThat

class UserControllerRestAssuredIT : AbstractIntegrationTest() {

    @Autowired
    internal var userRepository: UserRepository? = null

    @Autowired
    internal var objectMapper: ObjectMapper? = null

    lateinit var existingUser: UserDTO
    lateinit var newUser: UserDTO
    lateinit var updateUser: UserDTO

    @LocalServerPort
    private val serverPort: Int = 0

    @Before
    override fun setUp() {
        super.setUp()
        RestAssured.baseURI = "http://localhost:$serverPort"

        newUser = TestHelper.buildUser()

        existingUser = TestHelper.buildUser()
        existingUser = UserDTO.fromEntity(userRepository!!.save(existingUser.toEntity()))

        updateUser = TestHelper.buildUser()
        updateUser = UserDTO.fromEntity(userRepository!!.save(updateUser.toEntity()))
    }

    @After
    override fun tearDown() {
        super.tearDown()
        if (newUser.id != null) {
            userRepository!!.deleteById(newUser.id!!)
        }
        userRepository!!.deleteAll(
                userRepository!!.findAllById(asList<Long>(existingUser.id, updateUser.id)))
    }

    @Test
    fun should_get_all_users() {
        val response = given().`when`().get("/api/users").then().statusCode(200).extract().`as`(Array<UserDTO>::class.java)
        val users = asList(*response)
        assertThat(users).isNotEmpty
    }

    @Test
    fun should_get_user_by_id() {
        mockEngine.mockGetGithubUser(existingUser.githubUsername!!)
        mockEngine.mockGetGithubUserRepos(existingUser.githubUsername!!)

        val user = given()
                .`when`()
                .get("/api/users/" + existingUser.id!!)
                .then()
                .statusCode(200)
                .extract()
                .`as`(UserProfile::class.java)

        assertThat(user).isNotNull
    }

    @Test
    fun should_create_user() {
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
    fun should_update_user() {
        val updatedUser = given()
                .contentType("application/json")
                .body(updateUser)
                .`when`()
                .put("/api/users/" + updateUser.id!!)
                .then()
                .statusCode(200)
                .extract()
                .`as`(UserDTO::class.java)

        assertThat(updatedUser.id).isNotNull()
        assertThat(updatedUser.id).isEqualTo(updateUser.id!!)
        assertThat(updatedUser.email).isEqualTo(updateUser.email)
    }

    @Test
    fun should_delete_user() {

        given().`when`().get("/api/users/" + existingUser.id!!).then().statusCode(200)

        given().`when`().delete("/api/users/" + existingUser.id!!).then().statusCode(200)
        given().`when`().get("/api/users/" + existingUser.id!!).then().statusCode(404)
    }
}
