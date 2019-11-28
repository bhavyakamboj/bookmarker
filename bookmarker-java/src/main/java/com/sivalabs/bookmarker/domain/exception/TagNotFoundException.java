package com.sivalabs.bookmarker.domain.exception;

public class TagNotFoundException extends ResourceNotFoundException {
    public TagNotFoundException(String message) {
        super(message);
    }
}
