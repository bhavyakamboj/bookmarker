package com.sivalabs.bookmarker.common.web.advice

import com.sivalabs.bookmarker.common.model.ApiError
import com.sivalabs.bookmarker.exception.BookmarkerException
import com.sivalabs.bookmarker.exception.ResourceNotFoundException
import com.sivalabs.bookmarker.utils.logger
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    private val log = logger()

    @ExceptionHandler(value = [
        EmptyResultDataAccessException::class,
        ResourceNotFoundException::class
    ])
    fun handleNotFoundException(exception: Exception): ResponseEntity<*> {
        log.error(exception.localizedMessage, exception)
        return ResponseEntity(ApiError(exception.localizedMessage), NOT_FOUND)
    }

    @ExceptionHandler(value = [BookmarkerException::class])
    fun handleBookmarkerException(exception: Exception): ResponseEntity<*> {
        log.error(exception.localizedMessage, exception)
        return ResponseEntity(ApiError(exception.localizedMessage), BAD_REQUEST)
    }
}
