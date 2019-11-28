package com.sivalabs.bookmarker.domain.exception;

public class BookmarkNotFoundException extends ResourceNotFoundException {
    public BookmarkNotFoundException(String message) {
        super(message);
    }
}
