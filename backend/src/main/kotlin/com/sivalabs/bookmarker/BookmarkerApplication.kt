package com.sivalabs.bookmarker

import com.sivalabs.bookmarker.config.BookmarkerProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties(value = [BookmarkerProperties::class])
class BookmarkerApplication

fun main(args: Array<String>) {
    runApplication<BookmarkerApplication>(*args)
}
