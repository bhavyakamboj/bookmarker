package com.sivalabs.bookmarker.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.zalando.problem.ProblemModule
import org.zalando.problem.validation.ConstraintViolationProblemModule
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
internal class AppConfig {

    @Bean
    fun timedAspect(registry: MeterRegistry): TimedAspect {
        return TimedAspect(registry)
    }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    @Bean
    fun jacksonBuilder(): Jackson2ObjectMapperBuilder {
        val b = Jackson2ObjectMapperBuilder()
        b.modulesToInstall(ProblemModule(), JavaTimeModule(), ConstraintViolationProblemModule(), KotlinModule())
        return b
    }
}
