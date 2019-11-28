package com.sivalabs.bookmarker.common;

import lombok.extern.slf4j.Slf4j;
import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.sivalabs.bookmarker.utils.TestConstants.*;
import static com.sivalabs.bookmarker.utils.TestConstants.POSTGRES_DOCKER_PASSWORD;

@Slf4j
@ContextConfiguration(initializers = {TestContainersConfig.Initializer.class})
public class TestContainersConfig {

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
