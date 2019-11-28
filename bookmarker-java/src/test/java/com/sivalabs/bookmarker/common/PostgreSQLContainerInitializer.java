package com.sivalabs.bookmarker.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.sivalabs.bookmarker.utils.TestConstants.*;

@Slf4j
public class PostgreSQLContainerInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static PostgreSQLContainer sqlContainer;

    static {
        sqlContainer = new PostgreSQLContainer(POSTGRES_DOCKER_IMAGE)
                .withDatabaseName(POSTGRES_DOCKER_DATABASE_NAME)
                .withUsername(POSTGRES_DOCKER_USERNAME)
                .withPassword(POSTGRES_DOCKER_PASSWORD);
        sqlContainer.start();
    }

    public void initialize (ConfigurableApplicationContext configurableApplicationContext){
        TestPropertyValues.of(
                "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                "spring.datasource.username=" + sqlContainer.getUsername(),
                "spring.datasource.password=" + sqlContainer.getPassword()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }

}
