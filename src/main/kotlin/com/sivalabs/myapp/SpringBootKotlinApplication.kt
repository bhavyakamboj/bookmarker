package com.sivalabs.myapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCircuitBreaker
class KartApplication

fun main(args: Array<String>) {
    runApplication<KartApplication>(*args)
}
