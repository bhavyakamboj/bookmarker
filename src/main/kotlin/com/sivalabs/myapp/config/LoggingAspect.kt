package com.sivalabs.myapp.config

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import java.util.Arrays
import java.util.stream.Collectors

@Aspect
@Component
class LoggingAspect {

    private val log = LoggerFactory.getLogger(LoggingAspect::class.java)

    @Around("@within(com.sivalabs.myapp.config.Loggable) || @annotation(com.sivalabs.myapp.config.Loggable)")
    @Throws(Throwable::class)
    fun logMethodEntryExit(pjp: ProceedingJoinPoint): Any {

        val start = System.currentTimeMillis()

        var className = ""
        var methodName = ""
        if (log.isTraceEnabled) {
            className = pjp.signature.declaringTypeName
            methodName = pjp.signature.name

            val args = pjp.args
            var argumentsToString = ""
            if (args != null) {
                argumentsToString = Arrays.stream(args)
                        .map<String> { arg -> arg?.toString() }
                        .collect(Collectors.joining(","))
            }
            log.trace(
                    String.format("Entering method %s.%s(%s)", className, methodName, argumentsToString))
        }

        val result = pjp.proceed()

        if (log.isTraceEnabled) {
            val elapsedTime = System.currentTimeMillis() - start
            log.trace(
                    String.format(
                            "Exiting method %s.%s; Execution time (ms): %s", className, methodName, elapsedTime))
        }

        return result
    }
}
