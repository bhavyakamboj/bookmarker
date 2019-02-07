package com.sivalabs.bookmarker.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

open class BookmarkerException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
open class ResourceNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookmarkNotFoundException(message: String) : ResourceNotFoundException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(message: String) : ResourceNotFoundException(message)
