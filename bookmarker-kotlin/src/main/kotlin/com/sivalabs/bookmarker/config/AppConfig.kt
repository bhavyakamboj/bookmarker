package com.sivalabs.bookmarker.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
internal class AppConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    @Bean
    fun javaTimeModule(): JavaTimeModule {
        return JavaTimeModule()
    }

    @Bean
    fun kotlinModule(): KotlinModule {
        return KotlinModule()
    }
}
