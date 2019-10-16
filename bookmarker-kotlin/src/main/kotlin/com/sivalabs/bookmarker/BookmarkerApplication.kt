package com.sivalabs.bookmarker

import com.sivalabs.bookmarker.config.BookmarkerProperties
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.zalando.problem.spring.web.autoconfigure.security.SecurityConfiguration

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class, SecurityConfiguration::class])
@EnableConfigurationProperties(value = [BookmarkerProperties::class])
@EnableAspectJAutoProxy
@EnableCaching
class BookmarkerApplication {

    @Bean
    fun metricsCommonTags(): MeterRegistryCustomizer<MeterRegistry> {
        return MeterRegistryCustomizer { registry ->
            registry.config().commonTags("app", "bookmarker")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<BookmarkerApplication>(*args)
}
