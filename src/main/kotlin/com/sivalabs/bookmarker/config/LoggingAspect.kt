package com.sivalabs.bookmarker.config

import com.sivalabs.bookmarker.utils.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {

    private val log = logger()

    @Around("@within(com.sivalabs.bookmarker.config.Loggable) || @annotation(com.sivalabs.bookmarker.config.Loggable)")
    @Throws(Throwable::class)
    fun logMethodEntryExit(pjp: ProceedingJoinPoint): Any? {

        val start = System.currentTimeMillis()

        var className = ""
        var methodName = ""
        if (log.isTraceEnabled) {
            className = pjp.signature.declaringTypeName
            methodName = pjp.signature.name

            val args = pjp.args
            var argumentsToString = ""
            if (args != null) {
                argumentsToString = args.map { arg -> arg?.toString() }.joinToString(",")
            }
            log.trace("Entering method $className.$methodName($argumentsToString)")
        }

        val result = pjp.proceed()

        if (log.isTraceEnabled) {
            val elapsedTime = System.currentTimeMillis() - start
            log.trace("Exiting method $className.$methodName; Execution time: $elapsedTime ms")
        }

        return result
    }
}
