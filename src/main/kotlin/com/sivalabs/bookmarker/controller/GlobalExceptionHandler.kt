package com.sivalabs.bookmarker.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [EmptyResultDataAccessException::class])
    internal fun handleEmptyResultDataAccessException(exception: Exception): ResponseEntity<*> {
        return ResponseEntity(exception, HttpStatus.NOT_FOUND)
    }
}
