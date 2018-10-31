package com.sivalabs.kart

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KartApplication

fun main(args: Array<String>) {
    runApplication<KartApplication>(*args)
}
