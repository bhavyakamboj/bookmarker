package com.sivalabs.bookmarker.common.web.advice

import com.sivalabs.bookmarker.exception.BookmarkerException
import com.sivalabs.bookmarker.exception.ResourceNotFoundException
import com.sivalabs.bookmarker.utils.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status
import org.zalando.problem.spring.web.advice.ProblemHandling

@RestControllerAdvice
class GlobalExceptionHandler : ProblemHandling {
    private val log = logger()

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleResourceNotFoundException(
        exception: ResourceNotFoundException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        log.error(exception.localizedMessage, exception)
        return create(Status.NOT_FOUND, exception, request)
    }

    @ExceptionHandler(value = [BookmarkerException::class])
    fun handleBookmarkerException(exception: BookmarkerException, request: NativeWebRequest): ResponseEntity<Problem> {
        log.error(exception.localizedMessage, exception)
        return create(Status.BAD_REQUEST, exception, request)
    }
}
