package com.sivalabs.bookmarker.common

import com.sivalabs.bookmarker.domain.model.AuthenticationRequest
import com.sivalabs.bookmarker.domain.model.AuthenticationResponse
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
abstract class AbstractIntegrationTest {

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    protected val adminUserId = 1L
    protected var httpHeaders: HttpHeaders? = null

    protected val adminCredentials = Pair("admin@gmail.com", "admin")
    protected val user1Credentials = Pair("siva@gmail.com", "siva")
    protected val user2Credentials = Pair("prasad@gmail.com", "prasad")

    companion object {
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>()
    }

    init {
        postgresContainer.start()
    }

    protected fun asAuthenticateUser(credentials: Pair<String, String>) {
        httpHeaders = getAuthHeaders(credentials.first, credentials.second)
    }

    protected fun verifyStatusCode(responseEntity: ResponseEntity<*>, code: HttpStatus) {
        Assertions.assertThat(responseEntity.statusCode).isEqualTo(code)
    }

    protected fun getAuthHeaders(
        username: String = adminCredentials.first,
        password: String = adminCredentials.second
    ): HttpHeaders {
        val responseEntity = authenticate(username, password)
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer ${responseEntity.body?.accessToken}")
        return headers
    }

    protected fun authenticate(username: String, password: String): ResponseEntity<AuthenticationResponse> {
        val authenticationRequest = AuthenticationRequest(username, password)
        val request = HttpEntity(authenticationRequest)
        return restTemplate.postForEntity("/api/auth/login", request, AuthenticationResponse::class.java)
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgresContainer.getUsername(),
                "spring.datasource.password=" + postgresContainer.getPassword()
            )
                .applyTo(configurableApplicationContext.environment)
        }
    }
}
