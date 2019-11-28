package com.sivalabs.bookmarker.common

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer

@ContextConfiguration(initializers = [TestContainersConfig.Initializer::class])
open class TestContainersConfig {

    companion object {
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>()
    }

    init {
        postgresContainer.start()
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
