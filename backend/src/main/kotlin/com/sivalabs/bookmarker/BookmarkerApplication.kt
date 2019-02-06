package com.sivalabs.bookmarker

import com.sivalabs.bookmarker.config.BookmarkerProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties(value = [BookmarkerProperties::class])
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
