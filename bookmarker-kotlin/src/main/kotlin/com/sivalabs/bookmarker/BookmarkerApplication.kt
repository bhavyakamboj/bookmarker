package com.sivalabs.bookmarker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.zalando.problem.spring.web.autoconfigure.security.SecurityConfiguration

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class, SecurityConfiguration::class])
@EnableAspectJAutoProxy
@EnableCaching
class BookmarkerApplication

fun main(args: Array<String>) {
    runApplication<BookmarkerApplication>(*args)
}
