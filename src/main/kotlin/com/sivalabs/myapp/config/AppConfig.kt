package com.sivalabs.myapp.config

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestTemplate
import java.io.IOException

@Configuration
internal class AppConfig {

    @Value("\${github.authToken}")
    private lateinit var githubAuthToken: String

    @Bean
    fun timedAspect(registry: MeterRegistry): TimedAspect {
        return TimedAspect(registry)
    }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        val interceptors = listOf(HeaderRequestInterceptor("Authorization", "token $githubAuthToken"))
        builder.additionalInterceptors(interceptors)
        return builder.build()
    }
}

internal class HeaderRequestInterceptor(
        private val headerName: String,
        private val headerValue: String)
    : ClientHttpRequestInterceptor {

    @Throws(IOException::class)
    override fun intercept(
            request: HttpRequest,
            body: ByteArray,
            execution: ClientHttpRequestExecution): ClientHttpResponse {
        request.headers.add(headerName, headerValue)
        return execution.execute(request, body)
    }
}
