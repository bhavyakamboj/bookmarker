package com.sivalabs.bookmarker.web.advice;

import com.sivalabs.bookmarker.domain.exception.BookmarkerException;
import com.sivalabs.bookmarker.domain.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements ProblemHandling, SecurityAdviceTrait {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<Problem> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            NativeWebRequest request
    ) {
        log.error(exception.getLocalizedMessage(), exception);
        return create(Status.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(value = BookmarkerException.class)
    ResponseEntity<Problem> handleBookmarkerException(BookmarkerException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return create(Status.BAD_REQUEST, exception, request);
    }
}