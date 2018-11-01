package com.sivalabs.myapp.controller

import com.sivalabs.myapp.utils.MockEngine
import org.junit.*
import org.junit.runner.RunWith
import org.mockserver.client.server.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.junit.MockServerRule
import org.mockserver.socket.PortFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class AbstractIntegrationTest {

    lateinit var mockEngine: MockEngine

    companion object {
        private lateinit var mockServerClient: MockServerClient

        @BeforeClass
        @JvmStatic
        fun prepare() {
            mockServerClient = ClientAndServer.startClientAndServer(PortFactory.findFreePort())
        }

        @AfterClass
        @JvmStatic
        fun destroy() {
            mockServerClient.close()
        }
    }


    @Before
    fun setUp() {
        mockEngine = MockEngine(mockServerClient)
    }

    @After
    fun tearDown() {
        mockServerClient.reset()
    }
}
