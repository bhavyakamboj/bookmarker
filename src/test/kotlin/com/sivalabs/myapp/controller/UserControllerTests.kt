package com.sivalabs.myapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sivalabs.myapp.model.GitHubUserDTO
import com.sivalabs.myapp.model.UserDTO
import com.sivalabs.myapp.model.UserProfile
import com.sivalabs.myapp.service.UserService
import com.sivalabs.myapp.utils.TestHelper
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import java.util.Arrays.asList

@RunWith(SpringRunner::class)
@WebMvcTest(controllers = [UserController::class])
class UserControllerTests {

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    lateinit var existingUser: UserDTO
    lateinit var newUser: UserDTO
    lateinit var updateUser: UserDTO

    @Before
    fun setUp() {
        newUser = TestHelper.buildUserWithId()
        existingUser = TestHelper.buildUserWithId()
        updateUser = TestHelper.buildUserWithId()
    }

    @Test
    fun should_get_all_users() {
        given(userService.getAllUsers())
                .willReturn(asList(existingUser, updateUser))

        this.mockMvc
                .perform(get("/api/users"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<Any>(2)))
    }

    @Test
    fun should_get_user_by_id() {
        val gitHubUserDTO = GitHubUserDTO(existingUser.id, existingUser.name, "", 0, asList())
        val profile = UserProfile(existingUser.id, existingUser.name, existingUser.email, gitHubUserDTO)

        given(userService.getUserProfile(existingUser.id)).willReturn(Optional.of(profile))

        this.mockMvc
                .perform(get("/api/users/" + existingUser.id))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", `is`<Long>(existingUser.id)))
                .andExpect(jsonPath("$.name", `is`<String>(existingUser.name)))
                .andExpect(jsonPath("$.email", `is`<String>(existingUser.email)))
    }

    @Test
    fun should_create_user() {
        given(userService.createUser(newUser)).willReturn(newUser)

        this.mockMvc
                .perform(
                        post("/api/users/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", `is`<String>(newUser.name)))
                .andExpect(jsonPath("$.email", `is`<String>(newUser.email)))
    }

    @Test
    fun should_update_user() {
        given(userService.updateUser(existingUser)).willReturn(existingUser)

        this.mockMvc
                .perform(
                        put("/api/users/" + existingUser.id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", `is`<Long>(existingUser.id)))
                .andExpect(jsonPath("$.name", `is`<String>(existingUser.name)))
                .andExpect(jsonPath("$.email", `is`<String>(existingUser.email)))
    }

    @Test
    fun should_delete_user() {
        willDoNothing().given<UserService>(userService).deleteUser(existingUser.id)

        this.mockMvc.perform(delete("/api/users/" + existingUser.id))
                .andExpect(status().isOk)
    }
}
