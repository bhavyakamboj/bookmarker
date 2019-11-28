package com.sivalabs.bookmarker.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.sivalabs.bookmarker.utils.TestConstants.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {AbstractIntegrationTest.Initializer.class})
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private static PostgreSQLContainer sqlContainer;

    private static MockServerContainer mockServerContainer;

    static {
        sqlContainer = new PostgreSQLContainer(POSTGRES_DOCKER_IMAGE)
                .withDatabaseName(POSTGRES_DOCKER_DATABASE_NAME)
                .withUsername(POSTGRES_DOCKER_USERNAME)
                .withPassword(POSTGRES_DOCKER_PASSWORD);
        sqlContainer.start();

        mockServerContainer = new MockServerContainer();
        mockServerContainer.start();
    }

    protected MockServerClient mockServerClient = new MockServerClient(
            mockServerContainer.getContainerIpAddress(),
            mockServerContainer.getServerPort());


    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            log.info("mockserver.base-url=" + mockServerContainer.getEndpoint());
            TestPropertyValues.of(
                    "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + sqlContainer.getUsername(),
                    "spring.datasource.password=" + sqlContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}