package com.sivalabs.myapp.controller

import com.sivalabs.myapp.utils.MockEngine
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockserver.client.server.MockServerClient
import org.mockserver.junit.MockServerRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class AbstractIntegrationTest {

    @Rule
    var mockServerRule = MockServerRule(this)

    private val mockServerClient: MockServerClient? = null

    lateinit var mockEngine: MockEngine

    @Before
    open fun setUp() {
        mockEngine = MockEngine(mockServerClient!!)
    }

    @After
    open fun tearDown() {
        mockServerClient!!.reset()
    }

    companion object {

        private val log = LoggerFactory.getLogger(AbstractIntegrationTest::class.java)
    }
}
