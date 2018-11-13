package com.sivalabs.bookmarker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class BookmarkerApplication

fun main(args: Array<String>) {
    runApplication<BookmarkerApplication>(*args)
}
